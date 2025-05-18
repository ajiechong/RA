# from whoosh.index import create_in, open_dir
# from whoosh.fields import *
# from whoosh.qparser import QueryParser
# import os
# from utils.data_loader import PaperLoader
#
# class KeywordRetriever:
#     """基于Whoosh的关键词检索器"""
#
#     def __init__(self, index_path: str = "./keyword_index"):
#         self.schema = Schema(
#             doc_id=ID(stored=True),
#             title=TEXT(stored=True),
#             authors=KEYWORD(stored=True),
#             abstract=TEXT(stored=True),
#             full_text=TEXT(stored=True),
#             link=STORED
#         )
#         self.index_path = index_path
#         self._init_index()
#
#     def _init_index(self):
#         if not os.path.exists(self.index_path):
#             os.mkdir(self.index_path)
#             self.ix = create_in(self.index_path, self.schema)
#         else:
#             self.ix = open_dir(self.index_path)
#
#     def index_documents(self, documents: list[dict]):
#         """建立索引"""
#         writer = self.ix.writer()
#         for doc in documents:
#             writer.add_document(
#                 doc_id=doc["doc_id"],
#                 title=doc["title"],
#                 authors=",".join(doc["authors"]),
#                 abstract=doc["abstract"],
#                 full_text=doc["full_text"],
#                 link=doc["pdf_link"]
#             )
#         writer.commit()
#
#     def search(self, query: str, top_k: int = 5) -> list[dict]:
#         """关键词检索"""
#         with self.ix.searcher() as searcher:
#             parser = QueryParser("full_text", self.ix.schema)
#             q = parser.parse(query)
#             results = searcher.search(q, limit=top_k)
#
#             return [{
#                 "score": result.score,
#                 "content": result["full_text"],
#                 "metadata": {
#                     "title": result["title"],
#                     "authors": result["authors"].split(","),
#                     "link": result["link"]
#                 }
#             } for result in results]