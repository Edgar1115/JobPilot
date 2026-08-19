# JobPilot 实施进度记录（Progress Log）

> 依据《JobPilot 技术设计与 Vibe Coding 实施规格书 v2.0》记录：已完成工作、当前状态、环境注意事项与待办 Backlog。
>
> 更新规则：每个 Phase / 重要改动完成后更新本文件，并与 `docs/TECH_INDEX.md`、`docs/decisions/` 保持一致。

## 一、总体状态

| 维度 | 状态 |
|---|---|
| Core Phase | **Phase 0 完成** —— 工程骨架可运行（8080 / 8000 / MySQL / Redis / RabbitMQ 均正常） |
| Lab | 22 项 Backlog，均 `DISCOVERED`（见 `TECH_INDEX.md`） |
| Reference | 0 项 |
| 构建 | `jobpilot-server`: `mvn test` 通过（Spring Boot 3.5.16，中间件连接正常） |

## 二、已完成事项

### 2026-08-18 — 工程基线对齐（对齐 v1 要求，v2.0 兼容）

1. **Spring Boot 版本对齐 3.5.x**（规格书 #3 要求 3.5.x，原骨架为 4.1.0）
   - parent 版本：`4.1.0` → `3.5.16`（Maven Central 上 3.5.x 最新版）
   - 依赖命名恢复 3.x 风格：`spring-boot-starter-webmvc` → `spring-boot-starter-web`；4.x 拆分的测试依赖合并为 `spring-boot-starter-test`
   - Java 17 保持不变
2. **包名对齐**（规格书 #6 要求 `com/jobpilot/`）：`com.edgar.jobpilot` → `com.jobpilot`，主类与测试类迁移
3. **.gitignore**：追加 `.m2-repo/`、`.DS_Store`
4. **验证**：`mvn test` 通过

### 2026-08-19 — 仓库结构升级（规格书 v2.0 第 6 章）

1. **`jobpilot-server/`**：Java 主服务整体移入（`pom.xml`、`src/`、`.mvn/`、`mvnw`、`mvnw.cmd`、`HELP.md`、`.m2-repo/`）
2. **资源文件对齐**：`application.yaml` → `application.yml`；新增 `application-local.yml`（local profile 占位）；新建 `mapper/` 目录（MyBatis XML，`.gitkeep` 占位）
3. **`jobpilot-ai/`**：Python AI 服务骨架 —— `app/{api, schemas, services, prompts, rag, llm}` + `tests/`，各目录 README 说明职责（`main.py` 留待 Phase 0）
4. **`reference/`**：可复用模块目录骨架，README 写明晋升验收标准（规格书 6.8）
5. **`labs/`**：实验田六分类 `redis / mysql / rabbitmq / spring / concurrency / jvm`，各 README 列出候选实验（规格书 64.2）
6. **`docs/`**：
   - `TECH_INDEX.md`：全局技术索引，初始化 22 项 Backlog（规格书 6.4 / 64.2）
   - `modules/`：Reference 技术卡片模板（6.5）
   - `experiments/`：Lab 实验报告模板（6.6）
   - `decisions/`：ADR 记录说明
   - `sources/`：学习来源记录说明（6.7）
   - 规格书归档：v2.0 移入并重命名为正常文件名（去除 URL 编码 `%20`），v1 标注 `(v1)` 保留为历史
7. **`deploy/`**：目录就位（`docker-compose.yml` 待 Phase 0）
8. **根 `README.md`**：v2.0 三角色总览、结构图、当前状态、文档入口、构建命令
9. **验证**：`jobpilot-server` 下 `mvn -Dmaven.repo.local=.m2-repo test` 通过

### 2026-08-19 — Core Phase 0：工程骨架（配置 + 可运行）

1. **基础设施（docker compose，规格书 #59）**：`deploy/docker-compose.yml`
   - MySQL 8.4（utf8mb4，库 `jobpilot`，用户 `jobpilot`/`jobpilot123`，healthcheck + volume）
   - Redis 7（healthcheck + volume）
   - RabbitMQ 3.13-management（5672 + 15672 管理 UI，用户 `jobpilot`/`jobpilot123`）
   - 三个容器均 healthy
2. **应用配置（规格书 #58）**：`application.yml` 完整化 —— datasource / redis / rabbitmq / jackson / management(health) / jobpilot.jwt / jobpilot.ai，敏感值走环境变量并带本地默认值；`application-local.yml` 保留 local profile 覆盖
3. **依赖补充**：`spring-boot-starter-actuator`（Health）、`spring-boot-starter-jdbc`、`spring-boot-starter-data-redis`、`spring-boot-starter-amqp`
4. **统一响应与全局异常（规格书 #8 / #56）**：`com.jobpilot.common` 下新增
   - `Result<T>`（code/message/data/requestId）
   - `ErrorCode`（400xx/401xx/403xx/404xx/409xx/429xx/500xx/503xx 错误码）
   - `BusinessException`、`GlobalExceptionHandler`（@RestControllerAdvice，内部异常记完整堆栈、对外不暴露细节）
   - `PingController`（骨架验证接口 `/api/v1/ping`、`/api/v1/ping/error`）
5. **Python AI 服务（Phase 0 最小骨架）**：`jobpilot-ai/app/main.py`（FastAPI + `/health`）、`requirements.txt`、`.venv`（fastapi 0.141.1 / uvicorn 0.52.3）
6. **本机环境调整**：停止本机 brew Redis 与 /usr/local/mysql（3306/6379 原被占用），改为 Docker Desktop 统一管理三个中间件
7. **验证结果**
   - `http://localhost:8080/actuator/health` → `status: UP`，`db/redis/rabbit` 全部 `UP`
   - `GET /api/v1/ping` → `{"code":0,"message":"success","data":"pong","requestId":null}`
   - `GET /api/v1/ping/error` → `{"code":50000,"message":"skeleton validation exception",...}`（全局异常生效）
   - `http://localhost:8000/health` → `{"status":"UP","service":"jobpilot-ai","version":"0.1.0"}`
   - `mvn test`：Tests run 1, Failures 0, Errors 0，BUILD SUCCESS

### 2026-08-19 — API 文档 UI 升级：Swagger UI → Knife4j（ADR-001）

1. **背景**：springdoc 原生 Swagger UI 界面简陋、无中文、调试体验一般；规格书要求 API 文档统一管理元数据（`OpenApiConfig`），需要一个体验更好的展示层
2. **依赖**：`pom.xml` 新增 `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.6.0`；springdoc 显式锁定 2.8.17 不变（Maven 就近原则，解析无冲突）
3. **配置**：`application.yml` 新增 `knife4j.*` —— `enable: true`、中文界面（`language: zh_cn`）、自定义页脚、顶部搜索、显示 OpenAPI 地址
4. **注释对齐**：`OpenApiConfig.java` 访问入口注释更新为 `/doc.html`（推荐入口）
5. **验证结果**（实际启动服务 + 依赖树确认）
   - `http://localhost:8080/doc.html` → 200，Knife4j 中文增强 UI
   - `/swagger-ui.html`（原 Swagger UI）→ 302，仍可用（保留不删）
   - `/v3/api-docs` → OpenAPI JSON 正常，`OpenApiConfig` 元数据原样渲染
   - 依赖树：springdoc 2.8.17 + knife4j 4.6.0（core + openapi3-ui），`mvn compile` 通过

## 三、环境与注意事项

- **中间件**：由 Docker Desktop 统一管理（`docker compose -f deploy/docker-compose.yml up -d`）。
  本机原有 brew Redis 与 /usr/local/mysql 已停止（3306/6379 由 docker 容器接管）。
  RabbitMQ 管理台：http://localhost:15672 （`jobpilot`/`jobpilot123`）。
- **本机 Maven 仓库重定向**：Homebrew Maven 的 `settings.xml` 将 `localRepository` 指向
  `/opt/homebrew/opt/maven/libexec/mvn_repo`（当前权限不可写），因此构建必须显式指定项目内仓库：

  ```bash
  cd jobpilot-server
  mvn -Dmaven.repo.local=.m2-repo clean test
  ```

- **启动服务**：

  ```bash
  # 1) 中间件
  docker compose -f deploy/docker-compose.yml up -d
  # 2) Java 主服务（8080）
  cd jobpilot-server && mvn -Dmaven.repo.local=.m2-repo spring-boot:run
  # 3) Python AI 服务（8000）
  cd jobpilot-ai && .venv/bin/uvicorn app.main:app --host 0.0.0.0 --port 8000
  ```

- **IDEA 需重新导入**：Java 工程已移至 `jobpilot-server/` 子目录，`.idea/` 为本地未跟踪文件，路径已失效，需重新导入 Maven 工程。
- Phase 0 的 `Result.requestId` 目前为 null，将在 Phase 1 的 RequestIdFilter（规格书 #20/#47）中填充。

## 四、待办 Backlog

### Core Phase（严格按序，一次只做一个阶段）

- [x] **Phase 0 工程骨架**：Result / 全局异常 / Health / FastAPI 最小服务 / `deploy/docker-compose.yml`（MySQL + Redis + RabbitMQ management）—— 已验收：8080、8000、MySQL、Redis、RabbitMQ 正常
- [ ] **Phase 1 Authentication**：`sys_user` / Register / Login / JWT / FilterChain / Refresh / Logout
- [ ] **Phase 2 Resume + Job**：CRUD / Upload / Ownership / Redis Job Cache（Cache Aside + Null Cache）
- [ ] **Phase 3 Interview**（FakeAIService）：Session 状态机 / Message / Start / Answer / Finish
- [ ] **Phase 4 接 Python**：FastAPI / WebClient / LLM / SSE / Prompt —— 验收：token stream 可见、message 落 MySQL
- [ ] **Phase 5 Redis**：Interview Memory（LIST + LTRIM + TTL）/ Lua RateLimiter
- [ ] **Phase 6 RabbitMQ**：`ai_task` / Producer / Consumer / Report / Confirm / ACK / Idempotency
- [ ] **Phase 7 Reliability**：Retry / DLQ / DEAD / Crash Recovery
- [ ] **Phase 8 RAG**：Qdrant / Embedding / Knowledge / Retrieve
- [ ] **Phase 9 真实实验**：MySQL Index / Redis Cache / CompletableFuture / Load Testing / JVM —— 之后才产出可写入简历的性能数字

### Lab / Reference（可独立孵化，详见 `docs/TECH_INDEX.md`）

- [ ] Redis：`global-id` / `watchdog` / `stream` / `bitmap` / `hyperloglog` / `geo`
- [ ] MySQL：`index` / `mvcc` / `deadlock` / `pagination`
- [ ] RabbitMQ：`confirm` / `retry-dlq` / `delay-queue`
- [ ] Spring：`transaction` / `aop` / `bean-lifecycle`
- [ ] Concurrency：`thread-pool` / `completable-future` / `threadlocal`
- [ ] JVM：`gc` / `thread-dump`

## 五、关联文档

| 文档 | 路径 |
|---|---|
| 规格书 v2.0（权威） | `docs/JobPilot — 技术设计与 Vibe Coding 实施规格书 v2.0.md` |
| 技术索引 | `docs/TECH_INDEX.md` |
| 技术卡片 / 实验报告 / ADR / 来源 | `docs/modules/` `docs/experiments/` `docs/decisions/` `docs/sources/` |
| 项目总览 | `README.md` |
