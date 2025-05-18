from .vec_retriever import VectorRetriever
from .key_retriever import KeywordRetriever
from typing import List, Dict
import numpy as np
from utils.data_loader import PaperLoader

class HybridRetriever:
    """结合语义与关键词的混合检索"""
    
    def __init__(self):
        self.vector_retriever = VectorRetriever()
        self.keyword_retriever = KeywordRetriever()
    
    def build_index(self, csv_path: str):
        """构建双索引"""
        loader = PaperLoader(csv_path)
        docs = loader.get_documents()
        self.vector_retriever.index_documents(docs)
        self.keyword_retriever.index_documents(docs)
    
    def search(self, query: str, top_k: int = 5) -> List[Dict]:
        # 并行执行两种检索
        vector_results = self.vector_retriever.search(query, top_k*2)
        keyword_results = self.keyword_retriever.search(query, top_k*2)
        
        # 结果融合（RRF算法）
        combined = self._reciprocal_rank_fusion(vector_results, keyword_results)
        return combined[:top_k]
    
    def _reciprocal_rank_fusion(self, list_a: List[Dict], list_b: List[Dict], k=60):
        """混合排序算法"""
        rankings = {}
        
        # 记录每个文档的排名
        for idx, doc in enumerate(list_a):
            doc_id = doc["metadata"]["title"]
            rankings[doc_id] = rankings.get(doc_id, 0) + 1/(k + idx + 1)
        
        for idx, doc in enumerate(list_b):
            doc_id = doc["metadata"]["title"]
            rankings[doc_id] = rankings.get(doc_id, 0) + 1/(k + idx + 1)
        
        # 按综合得分排序
        sorted_docs = sorted(
({"score": score, **doc} for doc in (list_a + list_b) for score in [rankings.get(doc["metadata"]["title"], 0)]),
            key=lambda x: x["score"],
            reverse=True
        )
        
        # 去重
        seen = set()
        unique_docs = []
        for doc in sorted_docs:
            doc_id = doc["metadata"]["title"]
            if doc_id not in seen:
                seen.add(doc_id)
                unique_docs.append(doc)
        
        return unique_docs