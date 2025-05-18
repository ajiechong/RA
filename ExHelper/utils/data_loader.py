import pandas as pd
from pathlib import Path
from typing import List, Dict
import hashlib

class PaperLoader:
    """CSV论文数据加载与预处理"""
    
    def __init__(self, csv_path: str):
        self.df = pd.read_csv(csv_path)
        self._preprocess()
    
    def _preprocess(self):
        # 生成唯一文档ID
        self.df['doc_id'] = self.df['title'].apply(
            lambda x: hashlib.md5(x.encode()).hexdigest()[:8]
        )
        # 清理作者列表格式
        self.df['authors'] = self.df['authors'].apply(
            lambda x: [a.strip("' ") for a in x.strip("[]").split(',')]
        )
        # 合并文本字段
        self.df['full_text'] = self.df.apply(
            lambda row: f"Title: {row['title']}\nAuthors: {', '.join(row['authors'])}\nAbstract: {row['abstract']}",
            axis=1
        )
    
    def get_documents(self) -> List[Dict]:
        """转换为文档字典列表"""
        return self.df.to_dict('records')