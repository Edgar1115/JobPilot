# experiments — Lab 实验报告

每个重要 Lab 创建 `docs/experiments/{yyyy-mm-dd}-{experiment-name}.md`（规格书 v2.0 第 6.6 节）。

## 统一模板

```markdown
# 实验名称

## Hypothesis
想验证什么？

## Environment
JDK / Spring Boot / Redis / MySQL / OS / Hardware / Docker Version

## Variables
控制变量与实验变量。

## Procedure
完整复现步骤。

## Observation
日志、Redis Key、SQL、线程状态、MQ 状态等。

## Result
结果。

## Failure Cases
主动制造了哪些失败？

## Conclusion
最终确认了什么？

## Production Implication
如果进入真实项目，需要额外做什么？
```

当前状态：无实验报告。
