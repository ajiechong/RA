from fastapi import APIRouter, HTTPException
from fastapi.responses import StreamingResponse
from ExHelper.core.agents.literature import LiteratureAnalyst
from ExHelper.utils.config_loader import load_config

router = APIRouter(prefix="/api/v1")

@router.post("/chat/literature")
async def chat_with_literature_analyst(request: dict):
    """流式文献分析接口"""
    try:
        agent = LiteratureAnalyst(load_config("configs/roles/literature.yaml"))
        return StreamingResponse(
            agent.process(request),
            media_type="text/event-stream",
            headers={
                "X-Accel-Buffering": "no",
                "Content-Type": "text/event-stream; charset=utf-8"
            }
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))