from fastapi import FastAPI, HTTPException
from ExHelper.core.agents.literature import LiteratureAnalyst
from ExHelper.core.agents.paper_analyst import PaperAnalyst
from ExHelper.core.agents.base import AgentConfig
from ExHelper.config.roles import ROLE_CONFIGS

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