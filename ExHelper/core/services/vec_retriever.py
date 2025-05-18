from typing import List, Dict
import chromadb
from sentence_transformers import SentenceTransformer
from utils.data_loader import PaperLoader

class VectorRetriever:
    """基于ChromaDB的向量检索器"""
    
    def __init__(self, model_name: str = "BAAI/bge-base-en-v1.5"):
        self.model = SentenceTransformer(model_name)
        self.client = chromadb.PersistentClient(path="./chroma_db")
        self.collection = self._init_collection()
    
    def _init_collection(self):
        try:
            return self.client.get_collection("cs_papers")
        except:
            return self.client.create_collection("cs_papers")
    
    def index_documents(self, documents: List[Dict]):
        """索引文档"""
        ids = [doc["doc_id"] for doc in documents]
        texts = [doc["full_text"] for doc in documents]
        embeddings = self.model.encode(texts, normalize_embeddings=True)
        
        self.collection.add(
            embeddings=embeddings.tolist(),
            documents=texts,
            metadatas=[{
                "title": doc["title"],
                "authors": doc["authors"],
                "year": doc["submission_date"],
                "link": doc["pdf_link"]
            } for doc in documents],
            ids=ids
        )
    
    def search(self, query: str, top_k: int = 5) -> List[Dict]:
        """语义检索"""
        query_embedding = self.model.encode(query, normalize_embeddings=True).tolist()
        results = self.collection.query(
            query_embeddings=[query_embedding],
            n_results=top_k,
            include=["documents", "metadatas", "distances"]
        )
        
        return [{
            "score": 1 - distance,
            "content": doc,
            "metadata": meta
        } for doc, meta, distance in zip(
            results["documents"][0],
            results["metadatas"][0],
            results["distances"][0]
        )]