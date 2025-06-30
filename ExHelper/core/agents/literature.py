from .base import BaseAgent, AgentConfig
from core.services.vec_retriever import VectorRetriever
from core.services.key_retriever import KeywordRetriever
from core.services.hybrid_retriever import HybridRetriever

class LiteratureAnalyst(BaseAgent):
    """文献分析角色"""

    def _build_prompt(self, query: str, context: str, results: list) -> str:
        """构建文献分析提示模板"""
        return f"""
        【科研助手角色设定】
        {self.config.role_config['system_prompt']}
        
        【pdf解析的文档内容】
        {context}
        
        【输出要求】
        {self.config.role_config['output_format']}
        """
