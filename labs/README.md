# labs — 技术实验田

验证新学到的技术、原理与异常场景（规格书 v2.0 第 0.3 / 64.1 节）。

## 规则

- 一次 Lab 聚焦一个核心问题；
- 最小可运行实验，不追求业务 UI；
- 先验证原理，再封装；先记录失败，再记录成功；
- 主动制造失败/边界场景；
- 每个实验必须产出 `docs/experiments/{yyyy-mm-dd}-{name}.md` 实验报告（模板见 `../docs/experiments/README.md`）；
- 结论为 `VERIFIED / FAILED / NEED_MORE_TEST`；
- **`labs/` 默认不得被 Core 依赖，实验失败不能影响主项目启动**（规格书强制规则 16）；
- Lab 不进入主 Maven Reactor，可独立运行（规格书 6.2）。

## 分类

```text
redis/       global-id / watchdog / stream / bitmap / hyperloglog / geo
mysql/       index / mvcc / deadlock / pagination
rabbitmq/    confirm / retry-dlq / delay-queue
spring/      transaction / aop / bean-lifecycle
concurrency/ thread-pool / completable-future / threadlocal
jvm/         gc / thread-dump
```

Backlog 状态见 `../docs/TECH_INDEX.md`。
