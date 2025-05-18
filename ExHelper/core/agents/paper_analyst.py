from .base import BaseAgent, AgentConfig
from typing import AsyncGenerator
from .base import BaseAgent, AgentConfig


class PaperAnalyst(BaseAgent):
    """论文分析角色"""

    def _build_prompt(self, query: str, context: str, results: list) -> str:
        """构建论文分析提示模板"""
        return f"""
        【科研助手角色设定】
        {self.config.role_config['system_prompt']}
        
        【检索到的相关文档】
        {results[:3] if results else "无相关文档"}  # 显示前3个结果
        
        【当前对话上下文】
        {context}
        
        【用户问题】
        {query}
        
        【输出要求】
        {self.config.role_config['output_format']}
        """