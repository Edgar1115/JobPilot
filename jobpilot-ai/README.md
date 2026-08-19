# jobpilot-ai — Core：Python AI 服务

AI 推理侧服务，只负责 AI 能力，不直接操作 Java 核心业务表（规格书 v2.0 第 5 章）。

## 职责

```text
简历解析
LLM / Prompt
面试题生成
回答评分
报告生成
Embedding
RAG
Memory Summary
```

## 目录结构

```text
app/
├── main.py         # FastAPI 入口（Phase 0 创建）
├── api/            # /internal/ai/* 内部接口
├── schemas/        # Pydantic 模型
├── services/       # 业务编排
├── prompts/        # Prompt 模板（禁止写死在 service 中，带版本）
├── rag/            # 向量化 / 检索
└── llm/            # LLM 客户端封装
tests/              # pytest
```

## 与 Java 主服务的关系

- 仅通过 `/internal/ai/*` 暴露内部 API，鉴权使用 `X-Internal-Token` + `X-Request-Id` 透传。
- 调用方：Java 服务（WebClient），超时与重试策略见规格书 #50。

## 当前状态

Phase 0 之前：目录骨架已建，`app/main.py` 待 Phase 0 实现 FastAPI 最小服务。
