import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import re
import os
import joblib  # 用于保存/加载vectorizer


class VectorDatabase:
    def __init__(self, vector_file_path='article_vectors.npy', vectorizer_file_path='vectorizer.pkl'):
        self.vector_file_path = vector_file_path
        self.vectorizer_file_path = vectorizer_file_path
        self.vectorizer = TfidfVectorizer()
        self.tfidf_matrix = None
        self.metadata = pd.DataFrame()
        self.load_vectorizer()
        self.load_vectors()
        self.load_metadata()

    def load_vectorizer(self):
        if os.path.exists(self.vectorizer_file_path):
            self.vectorizer = joblib.load(self.vectorizer_file_path)

    def load_vectors(self):
        if os.path.exists(self.vector_file_path):
            self.tfidf_matrix = np.load(self.vector_file_path, allow_pickle=True)
            # 类型验证
            if self.tfidf_matrix.dtype == np.object_:
                raise ValueError("检测到旧的稀疏矩阵格式，请删除*.npy文件后重新初始化")

    def load_metadata(self):
        if os.path.exists('metadata.csv'):
            self.metadata = pd.read_csv('metadata.csv').fillna('')

    def save_vectorizer(self):
        joblib.dump(self.vectorizer, self.vectorizer_file_path)

    def save_vectors(self):
        if self.tfidf_matrix is not None:
            np.save(self.vector_file_path, self.tfidf_matrix)

    def save_metadata(self):
        self.metadata.to_csv('metadata.csv', index=False)

    def initialize_from_csv(self, csv_file_path):
        df = pd.read_csv(csv_file_path).fillna('')
        combined_text = df['title'] + " " + df['abstract']
        
        # 直接存储稀疏矩阵转换后的密集数组
        self.tfidf_matrix = self.vectorizer.fit_transform(combined_text).toarray()
        self.metadata = df[['title', 'authors', 'abstract', 'submission_date', 'pdf_link']]
        
        self.save_vectorizer()
        self.save_vectors()
        self.save_metadata()
        return df

    def add_new_article(self, title, abstract, authors, submission_date, pdf_link):
        new_text = title + " " + abstract
        new_vector = self.vectorizer.transform([new_text]).toarray()

        if self.tfidf_matrix is None:
            self.tfidf_matrix = new_vector
        else:
            try:
                self.tfidf_matrix = np.vstack([self.tfidf_matrix, new_vector])
            except ValueError as e:
                raise ValueError("特征维度不匹配，请确保使用相同的预处理流程") from e

        new_metadata = pd.DataFrame([{
            'title': title,
            'authors': authors,
            'abstract': abstract,
            'submission_date': submission_date,
            'pdf_link': pdf_link
        }])
        self.metadata = pd.concat([self.metadata, new_metadata], ignore_index=True)
        self.save_vectors()
        self.save_metadata()


class RAGQueryEngine:
    def __init__(self, vector_db):
        self.vector_db = vector_db

    def query_rag_system(self, query, fields=['title', 'abstract'], sort_by='relevance', top_n=5):
        # 文本预处理
        query = re.sub(r'[^\w\s]', '', query).lower()
        
        # 向量化查询
        query_vector = self.vector_db.vectorizer.transform([query]).toarray()
        
        # 计算相似度
        if self.vector_db.tfidf_matrix is None:
            return []
        similarity_scores = cosine_similarity(query_vector, self.vector_db.tfidf_matrix).flatten()

        # 关键词匹配
        regex = re.compile(query, re.IGNORECASE)
        keyword_matches = self.vector_db.metadata.apply(
            lambda row: any(regex.search(str(row[field])) for field in fields),
            axis=1
        )

        # 综合排序
        combined_scores = similarity_scores + keyword_matches.astype(float) * 0.5
        sorted_indices = np.argsort(combined_scores)[::-1]

        # 结果格式化
        results = []
        for idx in sorted_indices[:top_n]:
            row = self.vector_db.metadata.iloc[idx]
            result = {
                'Title': self._highlight(regex, row['title']),
                'Authors': ', '.join(eval(row['authors'])) if pd.notna(row['authors']) else '',
                'Abstract': self._highlight(regex, row['abstract']),
                'Date': row['submission_date'],
                'PDF': row['pdf_link'],
                'Score': f"{combined_scores[idx]:.2f}"
            }
            results.append(result)
        return results

    def _highlight(self, regex, text):
        return regex.sub(r'\033[1;31m\g<0>\033[0m', str(text))


if __name__ == "__main__":
    # 清理旧数据（首次运行时建议执行）
    for f in ['article_vectors.npy', 'vectorizer.pkl', 'metadata.csv']:
        if os.path.exists(f):
            os.remove(f)

    # 初始化数据库
    db = VectorDatabase()
    if not os.path.exists(db.vector_file_path):
        db.initialize_from_csv('cs_papers.csv')  # 替换为实际路径

    # 示例查询
    query_engine = RAGQueryEngine(db)
    print("首次查询结果：")
    for result in query_engine.query_rag_system("machine learning", top_n=2):
        print("\n".join(f"{k}: {v}" for k, v in result.items()))
        print("-" * 80)

    # 添加新论文
    db.add_new_article(
        title="Advanced Machine Learning Techniques",
        abstract="New approaches in deep learning optimization",
        authors="['Alice Smith', 'Bob Johnson']",
        submission_date="2024-03-20",
        pdf_link="http://example.com/new_paper.pdf"
    )

    # 再次查询
    print("\n更新后查询结果：")
    for result in query_engine.query_rag_system("deep learning", top_n=3):
        print("\n".join(f"{k}: {v}" for k, v in result.items()))
        print("-" * 80)