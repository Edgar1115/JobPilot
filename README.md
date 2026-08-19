# JobPilot

> Java 后端技术实验田、可复用模块库与 AI 智能求职平台
> 技术设计、实验记录与 Vibe Coding 实施规格书（v2.0）

JobPilot 同时承担三种角色：

| 角色 | 说明 | 目录 |
|---|---|---|
| **Core** | 真实业务主项目：AI 求职与模拟面试系统（可启动 / 演示 / 压测） | `jobpilot-server/` + `jobpilot-ai/` |
| **Reference** | 可复用实现手册：半年后重新打开也能快速迁移到新项目 | `reference/` + `docs/modules/` |
| **Lab** | 技术实验田：最小可运行验证 + 主动制造失败 + 记录结论 | `labs/` + `docs/experiments/` |

## 仓库结构

```text
jobpilot/
├── jobpilot-server/    # Core：Java 主服务（Spring Boot 3.5.x / Java 17 / MyBatis / MySQL / Redis / RabbitMQ）
├── jobpilot-ai/        # Core：Python AI 服务（FastAPI / LLM / RAG）
├── reference/          # Reference：可复用实现
├── labs/               # Lab：技术实验田（redis / mysql / rabbitmq / spring / concurrency / jvm）
├── docs/               # 技术索引、技术卡片、实验报告、ADR、学习来源、规格书
├── deploy/             # docker-compose 等部署配置（Phase 0 填充）
└── README.md
```

## 当前状态

- **Core Phase**：Phase 0 完成 —— 工程骨架可运行：8080（Spring Boot，health：db/redis/rabbit 全 UP）、
  8000（FastAPI）、MySQL 8.4 / Redis / RabbitMQ 由 docker compose 管理（`deploy/docker-compose.yml`）。
- **Lab**：Backlog 清单见 `docs/TECH_INDEX.md`（22 项，均为 DISCOVERED）。
- **Reference**：暂无，随 Lab 验证结果逐步沉淀。
- 详细进度见 `docs/PROGRESS.md`。

## 文档入口

| 文档 | 路径 |
|---|---|
| 规格书 v2.0（权威） | `docs/JobPilot — 技术设计与 Vibe Coding 实施规格书 v2.0.md` |
| 规格书 v1（历史） | `docs/JobPilot — 技术设计与 Vibe Coding 实施规格书 (v1).md` |
| 实施进度记录 | `docs/PROGRESS.md` |
| 技术索引 | `docs/TECH_INDEX.md` |
| Reference 技术卡片 | `docs/modules/` |
| Lab 实验报告 | `docs/experiments/` |
| 架构决策 ADR | `docs/decisions/` |
| 学习来源记录 | `docs/sources/` |

## 构建与启动

```bash
# 1) 中间件（MySQL 8.4 / Redis / RabbitMQ management）
docker compose -f deploy/docker-compose.yml up -d

# 2) Java 主服务（8080）—— 本机 Maven 仓库被 Homebrew settings 重定向，使用项目内仓库
cd jobpilot-server
mvn -Dmaven.repo.local=.m2-repo clean test
mvn -Dmaven.repo.local=.m2-repo spring-boot:run

# 3) Python AI 服务（8000）
cd jobpilot-ai
python3 -m venv .venv && .venv/bin/pip install -r requirements.txt
.venv/bin/uvicorn app.main:app --host 0.0.0.0 --port 8000
```

验证：`http://localhost:8080/actuator/health`（UP）、`http://localhost:8080/api/v1/ping`、`http://localhost:8000/health`、RabbitMQ 管理台 `http://localhost:15672`（`jobpilot`/`jobpilot123`）。
