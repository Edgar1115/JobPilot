# api — 内部接口

`/internal/ai/*`（规格书 #28）：

- `POST /internal/ai/interview/next-question/stream` — 面试下一题（SSE）
- `POST /internal/ai/interview/report` — 面试报告
- `POST /internal/ai/resume/parse` — 简历解析

Header：`X-Internal-Token`、`X-Request-Id`。
