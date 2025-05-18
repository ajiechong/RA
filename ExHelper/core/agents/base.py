from pydantic import BaseModel
from typing import Generator, Dict, Any
from ExHelper.core.services.vec_retriever import VectorRetriever
from ExHelper.core.services.key_retriever import KeywordRetriever
from ExHelper.core.services.hybrid_retriever import HybridRetriever
from ExHelper.model.model_factory import get_model

class AgentConfig(BaseModel):
    role_config: Dict[str, Any]
    tool_config: Dict[str, Any]

class BaseAgent:
    """所有角色的基类"""

    def __init__(self, config: AgentConfig):
        self.config = config
        self.model = get_model("agent_logger")  # 使用 get_model 显式加载模型
        self._init_tools()

    def _init_tools(self):
        """初始化工具"""
        self.tools = {
            # 这里可以扩展工具集
            'vec_retriever': VectorRetriever(),
            'key_retriever':KeywordRetriever(),
            'hybrid_retriever':HybridRetriever(),
        }

    def generate_response(self, query: str, context: str = "", results: list = None) -> str:
        """生成响应"""
        prompt = self._build_prompt(query, context, results)
        response = self.model.codegen(prompt)
        if response:
            return response[0]["response"]
        else:
            return "No response received."

    def _build_prompt(self, query: str, context: str, results: list) -> str:
        """构建提示模板"""
        raise NotImplementedError("Subclasses must implement _build_prompt.")
    
