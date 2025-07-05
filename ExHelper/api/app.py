import io
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

import re
import requests
from io import BytesIO
import pdfplumber


@app.post("/chat/{role}")
def chat_with_agent(role: str, request: dict):
    """文献分析接口"""
    agent = AGENTS.get(role)
    if not agent:
        raise HTTPException(status_code=404, detail=f"Role {role} not found")

    query = request.get("query", "")
    context = request.get("context", "")
    results = request.get("results", [])
    query += "context是pdf提取出来内容，按照context的格式返回即可"
    # 改进的PDF链接提取正则（允许URL后接标点符号）
    pdf_link = context
    print(f"PDF链接: {pdf_link}")

    try:
        pdf_content = extract_text_from_pdf_url(pdf_link)
        if pdf_content:
            # 将PDF内容添加到上下文中，限制长度为200,000字符
            context += f"\n\n从PDF提取的内容（链接: {pdf_link}）:\n{pdf_content[:200000]}..."
            print(f"成功添加PDF内容，长度: {len(pdf_content)}")
        else:
            context += f"\n\n[警告]无法从PDF提取文本内容: {pdf_link}"
    except Exception as e:
        context += f"\n\n[错误]解析PDF失败 ({pdf_link}): {str(e)}"
        print(f"PDF解析错误: {e}")

    try:
        response = agent.generate_response(query, context, results)
        return {"response": response}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


def extract_text_from_pdf_url(url: str, max_pages: int = None) -> str:
    """
    从PDF链接提取文本内容

    参数:
        url: PDF文件的URL
        max_pages: 限制提取的最大页数（可选），用于大文件处理
    """
    try:
        # 发送HTTP请求获取PDF内容，设置超时时间为60秒
        response = requests.get(url, timeout=60)
        response.raise_for_status()  # 检查请求是否成功

        # 验证内容类型是否为PDF
        content_type = response.headers.get('Content-Type', '')
        if 'pdf' not in content_type.lower():
            raise ValueError(f"预期PDF文件，但得到: {content_type}")

        # 使用BytesIO将响应内容转换为内存中的文件对象
        pdf_bytes = io.BytesIO(response.content)

        # 打开PDF文件
        with pdfplumber.open(pdf_bytes) as pdf:
            total_pages = len(pdf.pages)
            if max_pages and total_pages > max_pages:
                print(f"警告: PDF共有{total_pages}页，将仅处理前{max_pages}页")

            extracted_text = ""
            # 逐页提取文本
            for i, page in enumerate(pdf.pages, 1):
                if max_pages and i > max_pages:
                    break

                # 提取当前页文本
                page_text = page.extract_text() or ""

                # 添加页码标记和分隔符
                extracted_text += f"\n\n===== 第{i}页  ====="
                extracted_text += f"\n{page_text}"

            return extracted_text.strip()

    except requests.exceptions.RequestException as e:
        print(f"网络错误 ({url}): {e}")
        return None
    except Exception as e:
        print(f"PDF解析失败 ({url}): {e}")
        return None


# 定义RAG请求模型
class RAGQueryRequest(BaseModel):
    query: str
    fields: List[str] = ['title', 'abstract']
    top_n: int = 5
    context: str
    Title: str

# 初始化RAG引擎
vector_db = VectorDatabase()
rag_engine = RAGQueryEngine(vector_db)

# 新增RAG查询接口
@app.post("/rag/query")
def run_rag_query(request: RAGQueryRequest):
    context = request.context
    Title = request.Title
    pdf_link = context
    print(f"PDF链接: {pdf_link}")
    abstract = extract_text_from_pdf_url(pdf_link,1)
    request.query = f"{request.query}\n\n参考论文: {Title}\n摘要: {abstract}"
    try:
        results = rag_engine.query_rag_system(
            query=request.query,
            fields=request.fields,
            top_n=request.top_n,
        )
        return {"results": results, "total": len(results)}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

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
