"""JobPilot AI Service — FastAPI 入口（Phase 0 最小骨架）。

规格书 v2.0：Python 服务只负责 AI 推理（简历解析 / 面试 / 报告 / RAG），
内部接口统一挂在 /internal/ai/* 下，鉴权使用 X-Internal-Token。
"""

from fastapi import FastAPI

app = FastAPI(
    title="JobPilot AI Service",
    version="0.1.0",
    description="AI 推理服务：面试问答、报告生成、简历解析、RAG（/internal/ai/*）",
)


@app.get("/health")
def health() -> dict:
    """健康检查（Phase 0 验收：8000 正常）。"""
    return {"status": "UP", "service": "jobpilot-ai", "version": app.version}
