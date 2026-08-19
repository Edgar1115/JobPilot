# app — FastAPI 应用

| 子目录 | 职责 |
|---|---|
| `api/` | `/internal/ai/*` 内部接口（面试 / 报告 / 简历解析） |
| `schemas/` | Pydantic 请求/响应模型 |
| `services/` | 业务编排（Agent Workflow：LOAD_CONTEXT → EVALUATE_ANSWER → SELECT_TOPIC → GENERATE_QUESTION → OUTPUT） |
| `prompts/` | Prompt 模板文件（interviewer_system / answer_evaluation / report_generation / resume_parser / memory_summary，带版本） |
| `rag/` | Embedding / Qdrant 检索（Phase 8） |
| `llm/` | OpenAI-compatible LLM 客户端封装 |

入口：`main.py`（Phase 0 创建）。
