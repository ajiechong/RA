import os
from datetime import date
from typing import List, Optional

import uvicorn
from fastapi import FastAPI, HTTPException
import sys
from pathlib import Path
from openai import BaseModel
from openai import OpenAI
from dotenv import load_dotenv
from pydantic import Field

load_dotenv()

# 获取项目根目录路径
root_dir = Path(__file__).parent.parent
# 将项目根目录添加到系统路径
sys.path.append(str(root_dir))

from core.agents.literature import LiteratureAnalyst
from core.agents.paper_analyst import PaperAnalyst
from core.agents.base import AgentConfig
from config.roles import ROLE_CONFIGS

# 新增RAG相关导入
from core.rag.load_papers import VectorDatabase
from core.rag.load_papers import RAGQueryEngine

app = FastAPI(title="Research AI Assistant")

# 初始化核心角色
AGENTS = {
    'literature_analyst': LiteratureAnalyst(AgentConfig(
        role_config=ROLE_CONFIGS['literature']['role_config'],
        tool_config=ROLE_CONFIGS['literature']['tool_config']
    )),
    'paper_analyst': PaperAnalyst(AgentConfig(
        role_config=ROLE_CONFIGS['paper_analyst']['role_config'],
        tool_config=ROLE_CONFIGS['paper_analyst']['tool_config']
    ))
}

@app.get("/health")
def health_check():
    return {"status": "OK"}

@app.post("/chat/{role}")
def chat_with_agent(role: str, request: dict):
    """文献分析接口"""
    agent = AGENTS.get(role)
    if not agent:
        raise HTTPException(status_code=404, detail=f"Role {role} not found")

    query = request.get("query", "")
    context = request.get("context", "")
    results = request.get("results", [])

    try:
        response = agent.generate_response(query, context, results)
        return {"response": response}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# 定义RAG请求模型
class RAGQueryRequest(BaseModel):
    query: str
    fields: List[str] = ['title', 'abstract']
    top_n: int = 5

# 初始化RAG引擎
vector_db = VectorDatabase()
rag_engine = RAGQueryEngine(vector_db)

# 新增RAG查询接口
@app.post("/rag/query")
def run_rag_query(request: RAGQueryRequest):
    try:
        results = rag_engine.query_rag_system(
            query=request.query,
            fields=request.fields,
            top_n=request.top_n
        )
        return {"results": results, "total": len(results)}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

### 历史对话压缩接口
class ContextCompressionRequest(BaseModel):
    content: str

client = OpenAI(
    api_key=os.getenv("OPENAI_API_KEY"),  # 从环境变量获取 API Key
    base_url="https://api.agicto.cn/v1"
)
@app.post("/context/compress")
def compress_context(request: ContextCompressionRequest):
    """上下文压缩接口"""
    prompt = f"""请用中文为以下学术论文内容生成简洁摘要（300字内），指出其创新点和研究价值：
    {request.content} 
    你是一个专业的上下文压缩专家，擅长从对话历史中提取关键信息并压缩成300字以内的内容，保留所有重要细节和逻辑关系。
    请将历史对话中与当前问题相关的内容压缩成不多于300字的内容.
    """

    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.5,
            max_tokens=2048
        )
        print(response)
        content = response.choices[0].message.content.strip()
        print("压缩结果:", content)
        return {"compressed_context": content}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"上下文压缩失败: {str(e)}")


### rag新增文章接口
class ArticleRequest(BaseModel):
    title: str = Field(..., description="文章标题")
    abstract: str = Field(..., description="文章摘要")
    authors: List[str] = Field(..., description="作者列表")
    submission_date: Optional[str] = Field(date.today().isoformat(), description="提交日期 (YYYY-MM-DD)")
    pdf_link: Optional[str] = Field(None, description="PDF链接")


@app.post("/articles", response_model=dict, summary="添加新文章到向量数据库")
async def add_article(article: ArticleRequest):
    """
    添加新文章到向量数据库，并更新向量表示和元数据

    - **title**: 文章标题
    - **abstract**: 文章摘要
    - **authors**: 作者列表
    - **submission_date**: 提交日期 (默认为今日)
    - **pdf_link**: PDF链接 (可选)

    返回操作结果和新文章ID
    """
    try:
        # 将作者列表转换为字符串表示
        authors_str = str(article.authors)

        # 调用向量数据库的添加方法
        vector_db.add_new_article(
            title=article.title,
            abstract=article.abstract,
            authors=authors_str,
            submission_date=article.submission_date,
            pdf_link=article.pdf_link
        )

        # 获取新文章ID (假设为metadata的最后一行索引)
        new_article_id = len(vector_db.metadata) - 1

        return {
            "success": True,
            "message": "文章添加成功",
            "article_id": new_article_id,
            "total_articles": len(vector_db.metadata)
        }

    except ValueError as ve:
        raise HTTPException(status_code=400, detail=str(ve))
    except Exception as e:
        # 记录详细错误日志
        import traceback
        print(f"添加文章失败: {str(e)}")
        print(traceback.format_exc())
        raise HTTPException(status_code=500, detail="服务器内部错误")

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)
