# 技术索引（TECH_INDEX）

全局技术索引（规格书 v2.0 第 6.4 节）。新增任何重要技术模块都必须更新本表，确保可以通过“问题 / 技术 / 场景”快速找到代码和文档。

## 字段

| 字段 | 说明 |
|---|---|
| 模块 | 技术 / 模块名称 |
| 分类 | Core / Reference / Lab |
| 状态 | DISCOVERED / LAB / VERIFIED / REFERENCE / CORE / ARCHIVED |
| 解决的问题 | 一句话 |
| 业务场景 | JobPilot 中的位置 |
| Code | 代码路径 |
| Doc | 文档路径 |
| Source | 学习来源 |

## 初始 Backlog（来自规格书 64.2）

| 模块 | 分类 | 状态 | 场景 | Code | Doc | Source |
|---|---|---|---|---|---|---|
| JWT + Spring Security | Core | CORE | 认证主链路 | jobpilot-server/security, auth | docs/modules/ | - |
| Redis Cache Aside | Core | CORE | Job 热点缓存 | jobpilot-server/job | docs/modules/ | - |
| Redis Null Cache | Core | CORE | 缓存穿透 | jobpilot-server/job | docs/modules/ | - |
| Redis Lua Sliding Window | Core | CORE | AI API 限流 | jobpilot-server/infrastructure | docs/modules/ | - |
| Redis NX Idempotency | Core | CORE | 请求幂等 | jobpilot-server/infrastructure | docs/modules/ | - |
| RabbitMQ Reliability | Core | CORE | AI Report 长任务 | jobpilot-server/aitask | docs/modules/ | - |
| SSE | Core | CORE | AI 流式输出 | jobpilot-server/interview | docs/modules/ | - |
| CompletableFuture | Core | CORE | Context 并行加载 | jobpilot-server/interview | docs/modules/ | - |
| Redisson Distributed Lock | Lab / Reference | DISCOVERED | 锁续期 / 可重入 / 所有权 | labs/redis/watchdog | docs/experiments/ | - |
| Redis Global ID | Lab / Reference | DISCOVERED | 时间戳 + 自增序列 | labs/redis/global-id | docs/experiments/ | - |
| Redis Stream | Lab / Reference | DISCOVERED | Consumer Group / PEL | labs/redis/stream | docs/experiments/ | - |
| Bitmap | Lab | DISCOVERED | 签到类场景 | labs/redis/bitmap | docs/experiments/ | - |
| HyperLogLog | Lab | DISCOVERED | UV 统计 | labs/redis/hyperloglog | docs/experiments/ | - |
| Redis GEO | Lab | DISCOVERED | 附近位置检索 | labs/redis/geo | docs/experiments/ | - |
| Spring Cache | Reference | DISCOVERED | 与手写 Cache Aside 对比 | reference/ | docs/modules/ | - |
| WebSocket | Reference / Lab | DISCOVERED | 与 SSE 对比 | reference/websocket | docs/modules/ | - |
| Spring Task | Reference / Lab | DISCOVERED | 定时补偿 / 调度 | reference/ | docs/modules/ | - |
| RabbitMQ Delay Queue | Lab / Reference | DISCOVERED | 延迟重试与调度 | labs/rabbitmq/delay-queue | docs/experiments/ | - |
| MySQL Deadlock | Lab | DISCOVERED | 锁顺序与死锁恢复 | labs/mysql/deadlock | docs/experiments/ | - |
| @Transactional Failure Cases | Lab / Reference | DISCOVERED | 自调用 / 异常类型 / 代理机制 | labs/spring/transaction | docs/experiments/ | - |
| ThreadPool Rejection | Lab | DISCOVERED | 饱和与背压实验 | labs/concurrency/thread-pool | docs/experiments/ | - |
| JVM GC / Thread Dump | Lab | DISCOVERED | JVM 面试实验 | labs/jvm | docs/experiments/ | - |

> 维护规则：状态变化（LAB→VERIFIED→REFERENCE→CORE/ARCHIVED）时必须更新本表与对应 docs。
