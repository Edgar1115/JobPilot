# deploy — 部署配置

按规格书 v2.0 第 59 章，Phase 0 提供 `docker-compose.yml`：

```text
MySQL 8.4
Redis
RabbitMQ（management 镜像，含管理 UI，便于面试演示 Exchange / Queue / Consumer / DLQ）
```

Phase 4 追加 `Qdrant`（RAG 向量库）。Nginx 在部署阶段（Phase 9 之后）引入。

当前状态：Phase 0 之前，compose 文件待创建。
