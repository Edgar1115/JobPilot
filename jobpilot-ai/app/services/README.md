# services — 业务编排

受控 Agent Workflow（规格书 #27，禁止自由 Auto Agent）：

```text
LOAD_CONTEXT → EVALUATE_ANSWER → SELECT_TOPIC → GENERATE_QUESTION → OUTPUT
```

- interview.py：面试问答编排
- report.py：报告生成
- resume_parser.py：简历解析
- memory.py：长期摘要（Long-Term Memory）
