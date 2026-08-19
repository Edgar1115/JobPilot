# JobPilot — Java 后端技术实验田、可复用模块库与 AI 智能求职平台

## 技术设计、实验记录与 Vibe Coding 实施规格书（v2.0）

> **核心业务：** AI 智能求职与模拟面试平台。  
> **长期定位：** Java 后端技术实验田（Lab）+ 可复用实现手册（Reference）+ 可展示业务项目（Core）。  
> **核心原则：** 以真实可运行系统为基础；新技术先验证、再沉淀、后集成，不为了堆技术强行污染主业务。  
> **开发模式：** Java 单体主服务 + Python AI 服务 + MySQL + Redis + RabbitMQ；实验模块与主业务隔离。  
> **长期目标：** 将以后在教学视频、开源项目、工作项目和个人实践中学到的优秀后端模块，转化为可复现实验、可复用代码和可检索技术文档，使 JobPilot 成为个人长期维护的 Java Backend Knowledge Base。

---

# 0. 项目宪章：Core / Reference / Lab

JobPilot 不再只承担“做一个简历项目”的职责，而是同时承担三种角色。

## 0.1 Core — 真实业务主项目

Core 是可以完整启动、演示和压测的 AI 求职与模拟面试系统。

Core 中的技术必须满足：

```text
真实业务问题
    ↓
简单方案存在不足
    ↓
选择某项技术
    ↓
工程化实现
    ↓
测试 / 监控 / 异常处理
    ↓
能够解释取舍
```

Core 的主要价值：

- 简历项目；
- 面试项目；
- 系统设计练习；
- 集成验证；
- 性能实验；
- 将已验证技术放入真实业务环境。

禁止为了“显得高级”把与业务无关的组件强行接入 Core。

---

## 0.2 Reference — 可复用实现手册

Reference 用于保存以后可能在其他项目直接复用的成熟模块。

一个 Reference 模块至少应回答：

```text
它解决什么问题？
什么时候应该使用？
核心代码在哪里？
最小依赖是什么？
如何配置？
如何验证？
有哪些边界条件？
有哪些替代方案？
```

Reference 的目标不是“演示概念”，而是：

> **半年以后重新打开仓库，也可以快速理解并迁移到另一个项目。**

典型模块：

```text
JWT Authentication
Redis Cache Aside
Distributed Lock
Rate Limiter
Idempotency
RabbitMQ Reliable Consumer
SSE Streaming
WebSocket
File Upload
Operation Log
Thread Pool Configuration
```

---

## 0.3 Lab — 技术实验田

Lab 用于验证新学到的技术、原理和异常场景。

Lab 可以来自：

- 教学视频；
- GitHub / Gitee 项目；
- 其他个人项目；
- 工作实践；
- 技术文章；
- 面试题；
- 对 Core 中某项技术的进一步实验。

Lab 不要求一定进入主业务。

例如：

```text
Redisson WatchDog
Redis Global ID
Redis Stream Consumer Group
Bitmap Sign-In
HyperLogLog UV
GEO Nearby Search
RabbitMQ Delay Queue
@Transactional 失效场景
ThreadPool Rejection Policy
MySQL Deadlock
MVCC Isolation Experiment
JVM GC / Thread Dump
```

一个合格的 Lab 必须是可复现的实验，而不是只有一段代码。

---

## 0.4 三者关系

```text
学习来源
教学视频 / 项目 / 文档 / 面试题
        ↓
      DISCOVER
        ↓
       LAB
   最小可运行验证
        ↓
     VERIFIED
        ↓
    REFERENCE
   可复用实现沉淀
        ↓
业务真正需要时
        ↓
       CORE
```

注意：

- Lab 可以永远停留在 Lab；
- Reference 不一定进入 Core；
- Core 不依赖 Lab；
- 从 Lab 升级到 Core 时必须重新按照生产代码要求集成，而不是直接复制实验代码；
- 同一个技术允许同时存在“实验版本”和“生产版本”，因为二者目标不同。

---

# 1. Coding Agent 强制规则

在 Cursor、Claude Code、Codex、Pi 等工具中开发时，必须遵守：

1. 不自行改变本文定义的系统架构。
2. 当前采用 Java 单体主服务 + Python AI 服务，禁止初期拆 Spring Cloud 微服务。
3. Java 服务拥有用户、权限、MySQL 数据、Redis、MQ 和任务状态。
4. Python 服务只负责 AI 推理、RAG、简历解析、Embedding、报告生成。
5. Redis 不能成为业务事实数据源，最终业务数据必须落 MySQL。
6. MQ Consumer 必须具有幂等能力。
7. Redis 普通业务 Key 必须有 TTL。
8. 所有 HTTP 外部调用必须配置连接和读取超时。
9. 所有接口必须使用统一 Result 和全局异常处理。
10. 不允许伪造 QPS、响应时间、缓存命中率等数据。
11. 不为了“高级”主动增加 Kafka、K8s、Nacos、Seata、ES 等组件。
12. 严格按照开发 Phase 实现，一次只做一个阶段。
13. 每阶段结束必须保证项目能够启动、核心接口能够验证，再进入下一阶段。
14. 每次新增技术前，必须先分类为 `Core / Reference / Lab`，禁止默认塞入主业务。
15. Core 只能引入有真实业务理由的技术；如果回答不了“为什么这里需要它”，默认放入 Lab 或 Reference。
16. `labs/` 中的代码默认不得被 Core 直接依赖，实验失败不能影响主项目启动。
17. Reference 模块必须包含用途说明、最小配置、核心实现、验证方法、已知缺陷和替代方案。
18. Lab 必须记录实验目的、环境、步骤、观察结果和结论；只保存代码不算完成实验。
19. 从外部项目学习模块时，优先重新实现关键思想，不保存整个外部项目作为依赖；需要引用源码时记录来源与许可证。
20. 技术实验产生的 QPS、延迟、内存、吞吐量等数字必须保存实验条件，禁止脱离环境单独记录数字。
21. Lab 晋升为 Reference 或 Core 前必须经过一次整理：删除无关代码、补充测试、统一异常处理和配置。
22. Core 的开发 Phase 与 Lab 孵化相互独立；允许在 feature/lab 分支做实验，但不得以实验为理由提前实现后续 Core Phase。
23. 每新增一个重要技术模块，都必须更新技术索引，确保未来可以通过“问题/技术/场景”快速找到代码和文档。

---

# 2. 项目功能

用户能够：

- 注册、登录；
- 上传和管理简历；
- 添加目标公司和目标岗位 JD；
- 创建 AI 模拟面试；
- AI 根据简历、岗位 JD、历史表现动态生成问题；
- 用户回答以后，AI 对回答进行分析并继续追问；
- 使用 SSE 实现类似 ChatGPT 的流式输出；
- 保存完整面试历史；
- 异步生成最终面试报告；
- 统计 Java、MySQL、Redis、Spring、MQ 等知识维度评分；
- 根据历史面试生成能力画像；
- RAG 检索 Java 后端知识库。

---

# 3. 核心技术栈

## Java 主服务

- Java 17
- Spring Boot 3.5.x
- Spring Security
- MyBatis
- MySQL 8.4
- Redis
- RabbitMQ
- WebClient
- Jakarta Validation
- Jackson
- springdoc-openapi
- Maven
- JUnit 5
- Mockito
- Testcontainers（后期）
- Logback / SLF4J

## Python AI 服务

- Python 3.11+
- FastAPI
- Uvicorn
- Pydantic
- OpenAI-compatible LLM API
- 自定义 Agent Workflow
- Embedding
- Qdrant（RAG 阶段）
- pytest

## 基础设施

```text
MySQL
Redis
RabbitMQ
Qdrant（后期）
Nginx（部署阶段）
```

---

# 4. 总体架构

```text
                       Web Client
                           │
                    HTTP / SSE
                           │
                           ▼
               ┌────────────────────┐
               │ Java Spring Boot   │
               │   Main Service     │
               └─────────┬──────────┘
                         │
        ┌────────────────┼─────────────────┐
        │                │                 │
        ▼                ▼                 ▼
      MySQL            Redis           RabbitMQ
        │                │                 │
        │          Cache / Memory           │
        │          Rate Limit               ▼
        │          Idempotency         MQ Consumer
        │                                  │
        │                                  │ HTTP
        │                                  ▼
        │                       ┌───────────────────┐
        └──────────────────────▶│ Python FastAPI   │
                                │    AI Service    │
                                └────────┬──────────┘
                                         │
                                  ┌──────┴───────┐
                                  ▼              ▼
                                 LLM          Qdrant
```

---

# 5. 服务职责

## Java

负责：

```text
用户
认证
JWT
权限
MySQL
Redis
RabbitMQ
任务状态
业务事务
SSE
限流
幂等
日志
AI Service 调用
```

## Python

负责：

```text
简历解析
LLM
Prompt
面试题生成
回答评分
报告生成
Embedding
RAG
Memory Summary
```

Python 不直接操作 Java 的核心业务表。

---

# 6. Repository

推荐仓库结构升级为：

```text
jobpilot/
│
├── jobpilot-server/                 # Core：Java 主业务
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/jobpilot/
│       │   ├── common/
│       │   ├── config/
│       │   ├── security/
│       │   ├── auth/
│       │   ├── user/
│       │   ├── resume/
│       │   ├── job/
│       │   ├── interview/
│       │   ├── aitask/
│       │   ├── knowledge/
│       │   └── infrastructure/
│       └── resources/
│           ├── application.yml
│           ├── application-local.yml
│           └── mapper/
│
├── jobpilot-ai/                     # Core：Python AI 服务
│   ├── app/
│   │   ├── main.py
│   │   ├── api/
│   │   ├── schemas/
│   │   ├── services/
│   │   ├── prompts/
│   │   ├── rag/
│   │   └── llm/
│   └── tests/
│
├── reference/                       # Reference：可复用实现
│   ├── redis-cache-aside/
│   ├── redisson-lock/
│   ├── rabbitmq-reliable-consumer/
│   ├── sse-streaming/
│   ├── websocket/
│   └── ...
│
├── labs/                            # Lab：技术实验田
│   ├── redis/
│   │   ├── global-id/
│   │   ├── watchdog/
│   │   ├── stream/
│   │   ├── bitmap/
│   │   ├── hyperloglog/
│   │   └── geo/
│   ├── mysql/
│   │   ├── index/
│   │   ├── mvcc/
│   │   ├── deadlock/
│   │   └── pagination/
│   ├── rabbitmq/
│   │   ├── confirm/
│   │   ├── retry-dlq/
│   │   └── delay-queue/
│   ├── spring/
│   │   ├── transaction/
│   │   ├── aop/
│   │   └── bean-lifecycle/
│   ├── concurrency/
│   │   ├── thread-pool/
│   │   ├── completable-future/
│   │   └── threadlocal/
│   └── jvm/
│
├── docs/
│   ├── TECH_INDEX.md                # 全局技术索引
│   ├── modules/                     # Reference 技术卡片
│   ├── experiments/                 # Lab 实验报告
│   ├── decisions/                   # 重要架构决策 ADR
│   └── sources/                     # 学习来源记录
│
├── deploy/
│   └── docker-compose.yml
│
└── README.md
```

## 6.1 仓库隔离原则

```text
Core 可以独立启动
Reference 可以独立阅读 / 迁移
Lab 可以独立运行 / 失败
```

依赖方向：

```text
Core  ─X→ Labs
Core  ─X→ Reference Demo
Labs  ─X→ Core 业务内部实现
```

如果某个 Reference 模块已经被 Core 正式采用，应在 Core 中保留生产实现，同时在 Reference 文档中指向 Core 的实现位置，不要求维护两份完全相同的生产代码。

## 6.2 为什么不把所有实验做成 Maven 多模块

默认不把全部 Lab 加入主 Maven Reactor。

原因：

- 避免实验依赖污染 Core；
- 避免一次构建下载大量无关依赖；
- 允许不同实验使用不同版本；
- 某个实验失败不阻塞主项目编译；
- 保持主业务工程简单。

只有稳定、长期维护的 Reference 模块才考虑抽成正式 Maven Module / Starter。

## 6.3 技术模块生命周期

每个重要模块使用统一状态：

```text
DISCOVERED
    ↓
LAB
    ↓
VERIFIED
    ↓
REFERENCE
    ↓（业务需要）
CORE

任何阶段都可以：ARCHIVED
```

状态含义：

| 状态 | 含义 | 最低要求 |
|---|---|---|
| DISCOVERED | 刚发现，尚未实现 | 来源 + 想解决的问题 |
| LAB | 正在做实验 | 可运行代码 + 实验步骤 |
| VERIFIED | 已验证结论 | 结果 + 失败场景 + 结论 |
| REFERENCE | 可复用 | 文档 + 测试 + 最小依赖 + 使用示例 |
| CORE | 已进入主业务 | 真实场景 + 工程化 + 集成测试 |
| ARCHIVED | 暂不维护 | 归档原因 |

## 6.4 技术索引

`docs/TECH_INDEX.md` 至少维护以下字段：

```text
技术 / 模块名称
分类：Core / Reference / Lab
状态
解决的问题
业务场景
代码路径
文档路径
实验路径
学习来源
最后验证日期
```

推荐表格：

| 模块 | 分类 | 状态 | 场景 | Code | Doc | Source |
|---|---|---|---|---|---|---|
| Redis Cache Aside | Core | CORE | Job Detail Cache | ... | ... | ... |
| Redisson WatchDog | Lab | VERIFIED | 分布式锁续期实验 | ... | ... | ... |
| Redis Stream | Lab | LAB | 消费组 / Pending List | ... | ... | ... |

## 6.5 技术卡片模板

每个 Reference 或重要 Core 技术创建：

```text
docs/modules/{technology-name}.md
```

统一模板：

```markdown
# 技术名称

## 1. 解决什么问题
## 2. 典型使用场景
## 3. 为什么选择这个方案
## 4. 最小架构
## 5. 核心数据结构 / API
## 6. 核心代码位置
## 7. 配置
## 8. 正常流程
## 9. 异常流程
## 10. 并发 / 一致性问题
## 11. 测试方法
## 12. 已知缺陷
## 13. 替代方案
## 14. 什么时候不要用
## 15. JobPilot 中的位置
## 16. 面试追问
## 17. 学习来源
```

## 6.6 实验报告模板

每个重要 Lab 创建：

```text
docs/experiments/{yyyy-mm-dd}-{experiment-name}.md
```

统一模板：

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

## 6.7 从其他项目吸收模块的标准流程

以后在教学视频或其他项目看到优秀实现时，统一执行：

```text
1. 记录来源
      ↓
2. 写清它解决的问题
      ↓
3. 提取核心机制
      ↓
4. 在 Lab 最小复现
      ↓
5. 主动制造边界 / 异常场景
      ↓
6. 写实验结论
      ↓
7. 判断是否值得成为 Reference
      ↓
8. 如果 Core 确实需要，再做正式集成
```

禁止：

```text
看到一个完整项目
↓
整段复制
↓
不知道为什么能工作
↓
把技术名写进 README
```

## 6.8 晋升为 Reference 的验收标准

至少满足：

- 能独立运行或最小验证；
- 关键代码已经理解；
- 有正常测试；
- 有至少一个异常测试；
- 有配置说明；
- 有适用场景；
- 有不适用场景；
- 有替代方案；
- 有学习来源；
- 半年以后只看当前仓库仍然能复用。

## 6.9 晋升为 Core 的验收标准

除了 Reference 要求外，还必须满足：

1. JobPilot 存在真实业务需求；
2. 更简单方案存在明确不足；
3. 进入统一异常处理；
4. 进入日志 / RequestId / Observability；
5. 有必要的权限和安全检查；
6. 有集成测试；
7. 有配置隔离；
8. 不破坏现有 Phase；
9. 能够解释引入成本；
10. 能回答“如果删掉这个技术会怎样”。

---

# 7. Java 分层

每个业务模块：

```text
controller/
service/
service/impl/
mapper/
entity/
dto/
vo/
enums/
```

Controller 不直接：

- 写 SQL；
- 操作 Redis；
- 发送 MQ；
- 实现复杂业务。

核心逻辑进入 Service。

---

# 8. Result

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "requestId": "xxx"
}
```

Java：

```java
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private String requestId;
}
```

错误码范围：

```text
0       SUCCESS

400xx   参数/业务错误
401xx   Token/认证错误
403xx   权限错误
404xx   资源不存在
409xx   重复/状态冲突
429xx   限流

500xx   Server Error
503xx   AI/MQ 等外部服务不可用
```

---

# 9. 核心数据库表

至少包含：

```text
sys_user
resume
job_position
interview_session
interview_message
interview_report
ai_task
knowledge_document
user_skill_profile
operation_log
```

---

# 10. sys_user

```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    password_hash VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

# 11. resume

```sql
CREATE TABLE resume (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    file_url VARCHAR(500),
    raw_text MEDIUMTEXT,
    parsed_json JSON,
    parse_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_create (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

状态：

```text
PENDING
PARSING
SUCCESS
FAILED
```

---

# 12. job_position

```sql
CREATE TABLE job_position (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    company_name VARCHAR(100),
    position_name VARCHAR(100) NOT NULL,
    jd_text TEXT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_create (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

# 13. interview_session

```sql
CREATE TABLE interview_session (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    title VARCHAR(200),
    status VARCHAR(30) NOT NULL,
    current_round INT NOT NULL DEFAULT 0,
    max_round INT NOT NULL DEFAULT 10,
    started_at DATETIME(3),
    finished_at DATETIME(3),
    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_status_create (user_id, status, create_time),
    KEY idx_job (job_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

状态机：

```text
CREATED
   ↓
ACTIVE
   ↓
FINISHED
   ↓
REPORT_PENDING
   ↓
REPORTING
   ↓
COMPLETED
```

另外：

```text
CREATED / ACTIVE → ABORTED

REPORTING
    ↓ failure
REPORT_PENDING
```

---

# 14. interview_message

```sql
CREATE TABLE interview_message (
    id BIGINT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    round_no INT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    score DECIMAL(5,2),
    metadata JSON,
    create_time DATETIME(3) NOT NULL,

    KEY idx_session_round (session_id, round_no, id),
    KEY idx_user_create (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

role：

```text
SYSTEM
INTERVIEWER
USER
```

---

# 15. interview_report

```sql
CREATE TABLE interview_report (
    id BIGINT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    overall_score DECIMAL(5,2),
    java_score DECIMAL(5,2),
    database_score DECIMAL(5,2),
    redis_score DECIMAL(5,2),
    framework_score DECIMAL(5,2),
    project_score DECIMAL(5,2),

    summary TEXT,
    strengths JSON,
    weaknesses JSON,
    suggestions JSON,
    raw_report_json JSON,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_session (session_id),
    KEY idx_user_create (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

# 16. ai_task

这是整个项目非常关键的表。

```sql
CREATE TABLE ai_task (
    id BIGINT PRIMARY KEY,
    request_id VARCHAR(64) NOT NULL,
    user_id BIGINT NOT NULL,

    biz_type VARCHAR(50) NOT NULL,
    biz_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,

    retry_count INT NOT NULL DEFAULT 0,
    max_retry INT NOT NULL DEFAULT 3,

    error_message VARCHAR(1000),
    next_retry_time DATETIME(3),

    started_at DATETIME(3),
    finished_at DATETIME(3),

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_request_id (request_id),
    KEY idx_status_retry (status, next_retry_time),
    KEY idx_biz (biz_type, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

状态：

```text
PENDING
RUNNING
SUCCESS
FAILED
DEAD
```

---

# 17. user_skill_profile

```sql
CREATE TABLE user_skill_profile (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_code VARCHAR(50) NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    sample_count INT NOT NULL DEFAULT 0,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_user_skill (user_id, skill_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

示例：

```text
JAVA_CONCURRENT
JVM
MYSQL
REDIS
SPRING
MQ
NETWORK
PROJECT
AI_ENGINEERING
```

---

# 18. ID

业务主键采用：

```text
Snowflake BIGINT
```

MVP 先实现单 JVM Snowflake Generator。

不要初期单独创建 Redis ID Service。

面试准备：

```text
Auto Increment
UUID
Snowflake
Redis INCR
数据库号段模式
```

---

# 19. JWT

Access Token：

```text
30 min
```

至少包含：

```json
{
  "sub": "userId",
  "jti": "tokenId",
  "iat": 0,
  "exp": 0
}
```

Refresh Token：

```text
7 days
```

Redis：

```text
auth:refresh:{userId}:{tokenId}
```

JWT blacklist：

```text
auth:blacklist:{jti}
```

TTL 等于 Access Token 剩余有效期。

---

# 20. Security Filter

请求：

```text
HTTP
 ↓
RequestIdFilter
 ↓
JwtAuthenticationFilter
 ↓
SecurityFilterChain
 ↓
Controller
```

JWT Filter：

```text
获取 Header
↓
解析 Bearer Token
↓
验证 Signature
↓
验证 Expiration
↓
Redis 检查 Blacklist
↓
SecurityContext 设置 Authentication
```

---

# 21. Auth API

```http
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh
POST /api/v1/auth/logout
```

Login：

```json
{
  "username": "edgar",
  "password": "******"
}
```

Response：

```json
{
  "accessToken": "...",
  "refreshToken": "...",
  "expiresIn": 1800
}
```

---

# 22. Resume API

```http
POST   /api/v1/resumes
GET    /api/v1/resumes
GET    /api/v1/resumes/{id}
DELETE /api/v1/resumes/{id}

POST   /api/v1/resumes/{id}/parse
```

Parse：

```json
{
  "taskId": 5001,
  "status": "PENDING"
}
```

---

# 23. Job API

```http
POST /api/v1/jobs

GET /api/v1/jobs

GET /api/v1/jobs/{id}

PUT /api/v1/jobs/{id}
```

---

# 24. Interview API

创建：

```http
POST /api/v1/interviews
```

Request：

```json
{
  "resumeId": 1001,
  "jobId": 2001,
  "maxRound": 10
}
```

开始：

```http
POST /api/v1/interviews/{sessionId}/start
```

用户回答：

```http
POST /api/v1/interviews/{sessionId}/answer/stream

Accept: text/event-stream
```

获取历史：

```http
GET /api/v1/interviews/{sessionId}/messages
```

结束：

```http
POST /api/v1/interviews/{sessionId}/finish
```

报告：

```http
GET /api/v1/interviews/{sessionId}/report
```

Task：

```http
GET /api/v1/ai-tasks/{taskId}
```

---

# 25. Interview Start

```text
创建 Session
       ↓
检查 Resume ownership
       ↓
检查 Job ownership
       ↓
INSERT Session
       ↓
Start
       ↓
CREATED → ACTIVE
       ↓
并发加载 Resume / Job
       ↓
Python AI
       ↓
生成 Question
       ↓
INSERT Message
       ↓
Redis Short Memory
```

---

# 26. Answer 流程

```text
USER ANSWER
    ↓
检查 Session=ACTIVE
    ↓
INSERT USER MESSAGE
    ↓
Redis Memory
    ↓
加载：
 Resume
 Job
 Recent Memory
 Skill Profile
 RAG Context
    ↓
调用 Python SSE
    ↓
Java SSE Proxy
    ↓
Browser
    ↓
AI 完成生成
    ↓
INSERT INTERVIEWER MESSAGE
    ↓
current_round + 1
```

---

# 27. AI Agent

不要创建一个完全自由的 Auto Agent。

使用受控工作流：

```text
LOAD_CONTEXT
      ↓
EVALUATE_ANSWER
      ↓
SELECT_TOPIC
      ↓
GENERATE_QUESTION
      ↓
OUTPUT
```

这样更容易：

```text
debug
测试
复现
解释
```

---

# 28. Python API

```text
/internal/ai/*
```

Header：

```text
X-Internal-Token
X-Request-Id
```

面试：

```http
POST /internal/ai/interview/next-question/stream
```

报告：

```http
POST /internal/ai/interview/report
```

简历：

```http
POST /internal/ai/resume/parse
```

---

# 29. Prompt

不得写死在 Python Service 中。

目录：

```text
prompts/
├── interviewer_system.txt
├── answer_evaluation.txt
├── report_generation.txt
├── resume_parser.txt
└── memory_summary.txt
```

Prompt 带版本：

```text
interviewer-v1
```

---

# 30. Redis Key

统一规则：

```text
domain:purpose:id
```

例如：

```text
auth:refresh:1001:abc
auth:blacklist:xyz

job:detail:1001

interview:memory:3001

rate:ai:1001

idem:1001:finish:request123
```

禁止使用：

```text
abc
user1
data123
```

这种无语义 Key。

---

# 31. Job Cache

Key：

```text
job:detail:{jobId}
```

TTL：

```text
30 min + random(0~5 min)
```

读取：

```text
Redis
 ├── HIT → Return
 │
 └── MISS
       ↓
     MySQL
       ↓
     Redis
```

更新：

```text
UPDATE MYSQL
     ↓
COMMIT
     ↓
DELETE CACHE
```

即：

```text
Cache Aside
```

---

# 32. Cache Penetration

查询不存在 Job 时：

```text
cache null
```

TTL：

```text
1~3 min
```

Bloom Filter 只作为扩展讨论，MVP 不实现。

---

# 33. Interview Memory

Redis：

```text
interview:memory:{sessionId}
```

使用：

```text
LIST
```

只保存最近：

```text
10~20 messages
```

操作：

```text
RPUSH
LTRIM
EXPIRE
```

TTL：

```text
24h
```

完整数据在 MySQL。

这能够很好地回答：

```text
Redis Big Key
Memory
缓存和数据库职责
Agent Context
```

---

# 34. Rate Limit

AI API：

```text
每个用户
60 秒
最多 10 次
```

实现：

```text
Redis ZSET + Lua Sliding Window
```

Key：

```text
rate:ai:{userId}
```

Lua：

```text
删除窗口外数据
↓
ZCARD
↓
判断 limit
↓
ZADD
↓
EXPIRE
```

整个过程必须在一个脚本内完成。

超过：

```http
HTTP 429
```

---

# 35. Idempotency

关键接口：

```text
finish interview
parse resume
generate report
```

Header：

```text
X-Request-Id
```

Redis：

```text
idem:{userId}:{operation}:{requestId}
```

设置：

```text
SET key 1 NX EX 300
```

Redis：

```text
快速防重复
```

MySQL：

```text
UNIQUE(request_id)
```

作为最终兜底。

---

# 36. RabbitMQ

Exchange：

```text
jobpilot.ai.exchange
```

类型：

```text
direct
```

Queue：

```text
jobpilot.ai.resume.parse.queue
jobpilot.ai.report.generate.queue
```

Routing：

```text
resume.parse
report.generate
```

DLX：

```text
jobpilot.ai.dlx
```

DLQ：

```text
jobpilot.ai.resume.parse.dlq
jobpilot.ai.report.generate.dlq
```

---

# 37. MQ Message

禁止把完整简历/报告数据塞 MQ。

只发送 ID：

```json
{
  "messageId": "uuid",
  "taskId": 5001,
  "bizType": "INTERVIEW_REPORT",
  "bizId": 3001,
  "userId": 1001,
  "createdAt": "..."
}
```

消费者从 MySQL 加载业务数据。

---

# 38. MQ Reliability

项目目标：

```text
At-Least-Once
+
Idempotent Consumer
```

不是声称 Exactly Once。

Producer：

```text
@Transactional

INSERT ai_task(PENDING)
↓
Update Business Status
↓
COMMIT
↓
Publish MQ
```

启用：

```text
Publisher Confirm
```

如果 publish 失败：

```text
Task 仍为 PENDING
```

之后通过补偿程序重新发送。

---

# 39. Consumer Idempotency

收到 Message：

```text
SELECT ai_task
```

如果：

```text
SUCCESS
```

直接：

```text
ACK
```

如果：

```text
PENDING / FAILED
```

执行：

```sql
UPDATE ai_task
SET status = 'RUNNING',
    started_at = NOW(3)
WHERE id = ?
  AND status IN ('PENDING', 'FAILED');
```

只有：

```text
affectedRows == 1
```

的消费者获得任务执行权。

---

# 40. AI Report

Consumer：

```text
Message
 ↓
Load Session
 ↓
Load Messages
 ↓
Python /report
 ↓
Generate Report
 ↓
Transaction
 ├─ INSERT interview_report
 ├─ ai_task SUCCESS
 └─ session COMPLETED
 ↓
ACK
```

---

# 41. Retry

失败：

```text
retry_count++
```

如果：

```text
retry_count < max_retry
```

重新尝试。

超过：

```text
status = DEAD
```

并进入 DLQ。

---

# 42. Crash Recovery

解决典型问题：

> AI 运行了几十秒，服务器突然挂掉，任务怎么办？

定时：

```text
每分钟扫描
```

找：

```text
RUNNING
并且 started_at < now - 5min
```

判定 Zombie Task。

恢复：

```text
RUNNING
 ↓
FAILED
 ↓
retry_count++
 ↓
重新投递 MQ
```

最大重试次数必须有限。

---

# 43. CompletableFuture

AI Context 需要：

```text
Resume
Job
Recent Messages
Skill Profile
```

其中多个操作互相独立。

所以：

```java
CompletableFuture
```

并行加载。

禁止直接：

```text
ForkJoinPool.commonPool()
```

项目自己创建：

```text
aiContextExecutor
```

初始：

```text
corePoolSize = 4
maxPoolSize = 8
queueCapacity = 100
CallerRunsPolicy
```

这些只是初始值。

最终通过压测调整。

---

# 44. CompletableFuture vs MQ

这是面试必须能回答的问题。

## CompletableFuture

```text
短任务
同一进程
同一请求生命周期
最终请求需要等待
```

例如：

```text
并行读取 Resume / Job / Memory
```

## RabbitMQ

```text
长任务
请求可以先结束
需要 Retry
需要可靠投递
需要削峰
```

例如：

```text
生成最终 AI Report
```

---

# 45. Spring Transaction

典型：

```java
@Transactional
public Long finishInterview(...) {
    // update interview
    // create task
}
```

数据库：

```text
Session 修改
+
Task 创建
```

必须同一事务。

但：

```text
MySQL Transaction
```

和：

```text
RabbitMQ Publish
```

不是一个本地事务。

所以项目使用：

```text
DB Transaction
+
Message Compensation
```

---

# 46. AOP

注解：

```java
@OperationLog("Create Interview")
```

记录：

```text
requestId
userId
URI
method
costMs
success
exception
```

禁止记录：

```text
password
JWT
API Key
完整 Resume
```

对应面试知识：

```text
Spring AOP
JDK Dynamic Proxy
CGLIB
```

---

# 47. RequestId

客户端：

```text
X-Request-Id
```

没有则 Java 生成。

MDC：

```java
MDC.put("requestId", requestId);
```

调用 Python 时：

```text
透传
```

MQ：

```text
透传
```

最终：

```text
HTTP
 ↓
Java
 ↓
MQ
 ↓
Consumer
 ↓
Python
 ↓
LLM
```

可以用一个 requestId 串起来。

---

# 48. SSE

链路：

```text
Browser
 ↑
SSE
 ↑
Java SseEmitter
 ↑
WebClient
 ↑
Python SSE
 ↑
LLM Stream
```

Event：

```text
event: token
data: {"content":"HashMap"}
```

结束：

```text
event: done
data: {"messageId":123}
```

异常：

```text
event: error
data: {"code":50301}
```

---

# 49. Client Disconnect

浏览器关闭 SSE 后：

```text
Java completion/error callback
↓
cancel Python upstream
↓
释放资源
```

如果 AI 只生成半段：

```text
不保存为正式 interviewer message
```

只有完成以后保存。

---

# 50. AI Timeout

Interactive：

```text
connect timeout = 2s
first response timeout ≈ 15s
```

Report：

```text
更长 read timeout
```

禁止无限 Retry。

交互 AI 不可用：

```text
50301 AI_SERVICE_UNAVAILABLE
```

异步报告通过 Task/MQ Retry。

---

# 51. SQL Index 实验

目标 SQL：

```sql
SELECT id, title, status, create_time
FROM interview_session
WHERE user_id = ?
  AND status = ?
ORDER BY create_time DESC
LIMIT 20;
```

索引：

```text
(user_id, status, create_time)
```

实验：

```text
生成 10w+ rows
↓
无联合索引
↓
EXPLAIN ANALYZE
↓
记录
↓
创建联合索引
↓
再次测试
↓
比较
```

最终简历数字只能来自这个实验。

---

# 52. 深分页

第一版：

```sql
LIMIT 100000, 20
```

观察性能。

优化版：

```sql
WHERE user_id = ?
  AND id < ?
ORDER BY id DESC
LIMIT 20
```

也就是：

```text
Cursor Pagination
```

---

# 53. RAG

Phase 后期加入。

知识库：

```text
Java
JUC
JVM
MySQL
Redis
Spring
RabbitMQ
Network
System Design
```

流程：

```text
Document
 ↓
Parser
 ↓
Chunk
 ↓
Embedding
 ↓
Qdrant
```

Query：

```text
当前问题
+
用户回答
 ↓
Embedding
 ↓
Top-K
 ↓
Relevant Chunks
 ↓
Prompt
```

---

# 54. Long-Term Memory

Redis：

```text
短期上下文
```

MySQL：

```text
完整历史
```

长期摘要：

```json
{
  "weakTopics": ["AQS", "MVCC"],
  "strongTopics": ["Redis"],
  "commonMistakes": [],
  "summary": "..."
}
```

最终更新：

```text
user_skill_profile
```

---

# 55. 文件

MVP：

```text
local storage
```

路径：

```text
data/uploads/{userId}/
```

支持：

```text
PDF
DOCX
```

限制：

```text
10MB
```

后期可以替换：

```text
MinIO
```

但第一阶段不要加入。

---

# 56. 全局异常

```java
@RestControllerAdvice
```

处理：

```text
BusinessException
AuthenticationException
AccessDeniedException
ValidationException
AIServiceException
```

内部异常：

```text
Log 完整 Stack Trace
```

前端：

```text
只返回统一错误码
```

不能暴露：

```text
SQL
Stack Trace
Secret
Internal Path
```

---

# 57. Security

必须满足：

```text
BCrypt password

JWT Secret 环境变量

AI Key 环境变量

SQL 参数化

Ownership Check

Resume File Validation

敏感信息不进日志
```

例如：

```text
GET /resumes/1001
```

不能只根据：

```text
id=1001
```

查询。

必须：

```text
id=1001
AND user_id=currentUser
```

防止 IDOR。

---

# 58. application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jobpilot
    username: ${MYSQL_USER}
    password: ${MYSQL_PASSWORD}

  data:
    redis:
      host: localhost
      port: 6379

  rabbitmq:
    host: localhost
    port: 5672
    username: ${RABBITMQ_USER}
    password: ${RABBITMQ_PASSWORD}

jobpilot:
  jwt:
    secret: ${JWT_SECRET}
    access-ttl-seconds: 1800
    refresh-ttl-seconds: 604800

  ai:
    base-url: http://localhost:8000
    internal-token: ${AI_INTERNAL_TOKEN}
```

---

# 59. Docker Compose

初始只启动：

```text
MySQL
Redis
RabbitMQ
```

Phase 4：

```text
Qdrant
```

RabbitMQ 使用带：

```text
Management UI
```

的镜像。

这样面试演示可以直接看到：

```text
Exchange
Queue
Consumer
DLQ
```

---

# 60. Performance Test

必须真实测。

## Cache

```text
GET /jobs/{id}
```

比较：

```text
MySQL
vs
Redis Cache
```

记录：

```text
p50
p95
p99
QPS
```

## SQL

比较：

```text
Index Before
Index After
```

## CompletableFuture

比较：

```text
Sequential
vs
Parallel
```

## Rate Limit

并发请求验证：

```text
是否真的限制 10/min
```

---

# 61. JVM

后期实验：

```text
jps
jcmd
jstat
jstack
Arthas
JFR
```

做一个受控实验，例如：

```text
大量对象
Young GC

任务积压
线程池 Queue

慢线程
Thread Dump
```

让 JVM 八股能够和项目代码对应。

---

# 62. 必须覆盖的 25 个技术面问题

| # | 问题 | 项目代码 |
|---|---|---|
| 1 | JWT 怎么认证 | Security |
| 2 | JWT 为什么还用 Redis | refresh + blacklist |
| 3 | Spring Security FilterChain | Security |
| 4 | Spring AOP | operation log |
| 5 | Transaction 为什么会失效 | Interview Service |
| 6 | B+Tree | SQL optimization |
| 7 | 联合索引 | Session Index |
| 8 | 深分页 | Interview History |
| 9 | MVCC | Transaction |
| 10 | Redis 为什么快 | Cache |
| 11 | 缓存穿透 | Null Cache |
| 12 | 缓存雪崩 | TTL Jitter |
| 13 | 缓存击穿 | Enhancement |
| 14 | Cache Consistency | Cache Aside |
| 15 | Redis Big Key | AI Memory |
| 16 | Lua 原子性 | RateLimit |
| 17 | 幂等 | Redis NX + DB Unique |
| 18 | 线程池参数 | aiContextExecutor |
| 19 | CompletableFuture | Context Loader |
| 20 | MQ 重复消费 | AI Task |
| 21 | MQ 消息不丢 | Confirm / ACK |
| 22 | Consumer 崩溃 | Recovery |
| 23 | SSE vs WebSocket | Streaming |
| 24 | Agent Memory | Redis + MySQL |
| 25 | AI 长任务 Crash | ai_task |

---

# 63. 三个面试核心难点

不要把二十个组件全部当亮点。

最终只重点讲三个。

## 一、AI 异步任务可靠执行

```text
RabbitMQ
Task State Machine
Publisher Confirm
Manual ACK
Idempotency
Retry
DLQ
Crash Recovery
```

## 二、Agent Memory

```text
Redis Short Memory
MySQL Full History
Big Key Control
TTL
Long-Term Summary
RAG
```

## 三、缓存和数据库优化

```text
Cache Aside
Cache Penetration
TTL
MySQL Index
EXPLAIN ANALYZE
Performance Test
```

---

# 64. Core Vibe Coding Phase

以下 Phase 只描述 **Core 主业务** 的演进顺序。Lab / Reference 可以独立孵化，但不能以实验为理由提前改变 Core 架构。

## Phase 0

工程骨架。

```text
Spring Boot
FastAPI
Docker Compose
Result
Exception
Health
```

验收：

```text
8080 正常
8000 正常
MySQL 正常
Redis 正常
RabbitMQ 正常
```

---

## Phase 1

Authentication。

```text
sys_user
Register
Login
JWT
FilterChain
Refresh
Logout
```

---

## Phase 2

Resume + Job。

```text
Resume CRUD
Job CRUD
Upload
Ownership
Redis Job Cache
```

---

## Phase 3

Interview。

先不要 LLM。

用：

```text
FakeAIService
```

返回固定 Java 面试题。

实现：

```text
Session
State Machine
Message
Start
Answer
Finish
```

这一阶段只验证业务模型。

---

## Phase 4

接 Python。

```text
FastAPI
WebClient
LLM
SSE
Prompt
```

验收：

```text
能够看到 token stream
最终 message 入 MySQL
```

---

## Phase 5

Redis。

```text
Interview Memory
LTRIM
TTL
Lua RateLimiter
```

---

## Phase 6

RabbitMQ。

```text
ai_task
Producer
Consumer
Report
Confirm
ACK
Idempotency
```

---

## Phase 7

Reliability。

主动让：

```text
Python /report → HTTP 500
```

验证：

```text
Retry
DLQ
DEAD
Crash Recovery
```

---

## Phase 8

RAG。

```text
Qdrant
Embedding
Knowledge
Retrieve
Prompt
```

---

## Phase 9

真实实验。

```text
MySQL Index
Redis Cache
CompletableFuture
Load Testing
JVM
```

最终才生成可写入简历的性能数字。

---

## 64.1 Continuous Lab Track

Core Phase 之外，长期维护一条独立技术孵化线：

```text
Backlog
 ↓
Pick One Technology
 ↓
Minimal Lab
 ↓
Failure Experiment
 ↓
Document
 ↓
Verified
 ↓
Reference / Archive
 ↓
Need in Core?
 ├─ No  → Keep Reference
 └─ Yes → Production Integration
```

要求：

- 一次 Lab 聚焦一个核心问题；
- Lab 不追求复杂业务 UI；
- 优先使用最少代码暴露技术机制；
- 先验证原理，再封装；
- 先记录失败，再记录成功；
- 不要求每个 Lab 都写进简历；
- 只有真正形成项目价值的模块才进入 Core 技术亮点。

## 64.2 初始技术孵化清单

结合当前 Core 与后续 Java 后端学习，第一批可以维护：

| 技术 | 初始分类 | 建议状态 | 说明 |
|---|---|---|---|
| JWT + Spring Security | Core | CORE | 认证主链路 |
| Redis Cache Aside | Core | CORE | Job 热点缓存 |
| Redis Null Cache | Core | CORE | 缓存穿透 |
| Redis Lua Sliding Window | Core | CORE | AI API 限流 |
| Redis NX Idempotency | Core | CORE | 请求幂等 |
| RabbitMQ Reliability | Core | CORE | AI Report 长任务 |
| SSE | Core | CORE | AI 流式输出 |
| CompletableFuture | Core | CORE | Context 并行加载 |
| Redisson Distributed Lock | Lab / Reference | DISCOVERED | 学习锁续期、可重入、所有权 |
| Redis Global ID | Lab / Reference | DISCOVERED | 时间戳 + 自增序列实验 |
| Redis Stream | Lab / Reference | DISCOVERED | Consumer Group / PEL |
| Bitmap | Lab | DISCOVERED | 签到类场景 |
| HyperLogLog | Lab | DISCOVERED | UV 统计 |
| Redis GEO | Lab | DISCOVERED | 附近位置检索 |
| Spring Cache | Reference | DISCOVERED | 与手写 Cache Aside 对比 |
| WebSocket | Reference / Lab | DISCOVERED | 与 SSE 对比 |
| Spring Task | Reference / Lab | DISCOVERED | 定时补偿 / 调度 |
| RabbitMQ Delay Queue | Lab / Reference | DISCOVERED | 延迟重试与调度 |
| MySQL Deadlock | Lab | DISCOVERED | 锁顺序与死锁恢复 |
| @Transactional Failure Cases | Lab / Reference | DISCOVERED | 自调用、异常类型、代理机制 |
| ThreadPool Rejection | Lab | DISCOVERED | 饱和与背压实验 |
| JVM GC / Thread Dump | Lab | DISCOVERED | JVM 面试实验 |

这个表是 Backlog，不要求一次性实现。

---

# 65. 给 Coding Agent 的标准 Prompt

每一阶段都使用：

```text
你正在开发 JobPilot 项目。

请严格遵循《JobPilot 技术设计与 Vibe Coding 实施规格书》。

当前只执行 Phase X，不要提前开发后续 Phase。

本阶段目标：
[复制 Phase 内容]

要求：

1. 首先读取当前项目代码。
2. 不重复创建已有功能。
3. 先列出需要新增和修改的文件。
4. 再逐个实现。
5. 不得自行修改既有 API 和数据库设计。
6. 保证 Maven 编译通过。
7. 给出启动方法。
8. 给出正常测试场景。
9. 给出异常测试场景。
10. 完成以后说明当前 Phase 的验收方法。
11. 可以列出下一阶段任务，但禁止提前实现。
```


## 65.1 Lab Coding Agent Prompt

```text
你正在维护 JobPilot 技术实验田。

本次任务属于 Lab，不允许修改 Core 业务。

实验主题：
[技术名称]

要验证的问题：
[Hypothesis]

要求：
1. 先读取已有 labs 与 docs，避免重复实验。
2. 建立最小可运行实验，不加入无关业务代码。
3. 明确实验环境与版本。
4. 给出需要新增/修改的文件。
5. 实现正常场景。
6. 至少实现一个失败或边界场景。
7. 给出可观察证据：日志、Redis、SQL、MQ、线程状态等。
8. 生成对应 docs/experiments 实验报告。
9. 判断结果是 VERIFIED / FAILED / NEED_MORE_TEST。
10. 禁止自动把实验代码接入 Core。
```

## 65.2 Reference Coding Agent Prompt

```text
你正在把一个已经验证的 JobPilot Lab 整理成 Reference 模块。

目标：
未来在其他 Java 项目中可以快速理解并迁移这个实现。

要求：
1. 读取原 Lab、实验报告和现有 Core 实现。
2. 删除与实验无关的代码。
3. 最小化依赖。
4. 抽离配置。
5. 保留正常示例。
6. 保留关键异常示例。
7. 增加测试。
8. 写 docs/modules 技术卡片。
9. 明确适用 / 不适用场景。
10. 列出替代方案。
11. 更新 docs/TECH_INDEX.md。
12. 禁止因为整理 Reference 自动修改 Core。
```

---

# 66. 更细粒度 Vibe Coding

例如 Redis：

```text
读取当前 JobPilot 项目。

只实现 Job Detail Redis Cache，不修改其他模块。

要求：

Key:
job:detail:{jobId}

模式：
Cache Aside

TTL：
30 min + random 0~5 min

Cache Miss：
查询 MySQL

不存在：
缓存 null 1~3 min

Update Job：
数据库事务完成后删除缓存

禁止：
Redis KEYS

增加：
cache hit/miss debug log

必须：
提供验证步骤和测试。
```

这种 Prompt 的稳定性远高于：

```text
帮我优化 Redis。
```

---

# 67. README

最终必须展示：

```text
项目背景

架构图

技术栈

Docker 启动

API

数据库

业务流程

技术难点

MQ Reliability

Redis Design

Agent Memory

RAG

SQL Optimization

Performance Result

Screenshot

Technical Index

Reference Modules

Lab Catalogue

Experiment Reports

Architecture Decisions
```

---

# 68. Git Commit

建议：

```text
feat(auth): implement JWT authentication

feat(resume): add resume management

feat(job): add target position management

feat(cache): add Redis cache for job details

feat(interview): implement interview state machine

feat(ai): integrate FastAPI interview service

feat(stream): implement SSE interview streaming

feat(mq): add async report generation

feat(task): implement retry and crash recovery

feat(rag): add interview knowledge retrieval

perf(mysql): optimize interview history query

lab(redis): verify redisson watchdog renewal

lab(mysql): reproduce transaction deadlock

ref(redis): add reusable distributed lock example

ref(stream): document redis stream consumer group

docs(module): add redis rate limiter technical card

docs(experiment): record thread pool rejection test
```

---

# 69. 简历原则

开发前可以写：

> 基于 Spring Boot、MySQL、Redis 与 RabbitMQ 构建 AI 模拟面试平台，采用 Java 主服务与 Python AI 服务分层架构，实现 JWT 鉴权、流式 AI 对话、异步报告生成与历史面试管理。

可以写：

> 针对 AI 长耗时任务设计 RabbitMQ 异步执行机制，通过任务状态机、消费幂等、失败重试、死信队列及超时任务补偿提高任务执行可靠性。

可以写：

> 使用 Redis 实现热点数据缓存、AI 短期 Memory、接口限流与请求幂等，并基于 Cache Aside 设计缓存更新流程。

暂时不能写：

```text
QPS 提高 300%

Latency 降低 85%

支持 10 万并发

缓存命中率 99%
```

除非真实测出来。

---

# 70. 最终演示

```text
Login
 ↓
Resume
 ↓
Job
 ↓
Start Interview
 ↓
SSE AI Question
 ↓
Answer
 ↓
Finish
 ↓
RabbitMQ Task
 ↓
Task RUNNING
 ↓
Report SUCCESS
 ↓
查看 Redis Memory
 ↓
查看 RabbitMQ
 ↓
查看 MySQL
```

---

# 71. 最终项目故事

整个项目演进应该能讲成：

```text
最初只是同步 AI Interview
        ↓
AI Report 耗时比较长
        ↓
RabbitMQ Async
        ↓
出现失败与重复问题
        ↓
Task State Machine
Idempotency
Retry
DLQ
Crash Recovery
        ↓
对话越来越长
        ↓
Redis Short Memory
MySQL Full History
        ↓
需要专业知识
        ↓
RAG
        ↓
热点数据增多
        ↓
Redis Cache Aside
        ↓
查询规模变大
        ↓
Index Optimization
        ↓
Context 加载耗时
        ↓
CompletableFuture
```

面试时重点讲：

> **问题是什么 → 为什么选这个技术 → 怎么实现 → 有什么缺陷 → 如果规模继续增长下一步怎么办。**

而不是：

> “我的项目用了 Spring Boot、Redis、RabbitMQ、RAG……”

---

# 72. 最终定义

JobPilot 的目标不再只是做一个理论上的“大型互联网系统”，也不只是为了简历堆技术。

最终定义为：

> **一个可以长期维护的 Java 后端技术实验田、可复用模块库和真实业务项目。**

它同时服务三个长期目标。

## 72.1 Portfolio

Core 必须：

- 能真实运行；
- 能真实演示；
- 能真实压测；
- 关键技术有真实业务场景；
- 简历上的数字来自真实实验；
- 面试官连续追问 3～5 层时有代码和实验支撑。

## 72.2 Reference

以后开发其他项目时，希望实现：

```text
需要 JWT
  ↓
打开 JobPilot 技术索引
  ↓
找到 Authentication Reference
  ↓
查看代码 + 配置 + 测试 + 注意事项
  ↓
迁移到新项目
```

而不是重新寻找以前学过的整个课程或整个项目。

## 72.3 Lab

以后每学到一个值得研究的模块：

```text
发现
 ↓
最小复现
 ↓
制造失败
 ↓
观察内部机制
 ↓
写结论
 ↓
沉淀 Reference
```

项目允许持续增长，但增长单位应该是“被理解并验证的能力”，而不是“新增了多少依赖”。

---

# 73. 新技术准入的四个问题

以后任何新增技术，先回答：

1. **它解决什么具体问题？**
2. **我想学习它，还是主业务真的需要它？**
3. **它应该进入 Lab、Reference 还是 Core？**
4. **半年以后我怎样通过 JobPilot 快速重新找到并使用它？**

如果第 1 个问题回答不了：

> 不实现。

如果只是为了学习：

> 进入 Lab。

如果已经验证、以后可能复用：

> 晋升 Reference。

如果 JobPilot 真实业务需要：

> 按生产标准进入 Core。

---

# 74. 项目长期成长模型

```text
外部知识
教学视频 / GitHub / 工作项目 / 技术文章 / 面试题
        ↓
      JobPilot Lab
        ↓
  可复现实验与失败案例
        ↓
   JobPilot Reference
        ↓
 可复用代码 + 技术卡片
        ↓
业务需要时进入 Core
        ↓
真实业务 / 压测 / 面试故事
        ↓
形成个人 Java Backend Knowledge Base
```

最终希望 JobPilot 解决的不是“我做过多少项目”，而是：

> **我学过的后端能力，能不能被长期保存、快速检索、重新运行、再次验证，并在新的项目里复用。**

---

# 75. 最终成功标准

当 JobPilot 长期维护成熟后，应达到：

```text
看到一个后端技术
我知道它解决什么问题

需要验证原理
我有 Lab

需要重新回忆
我有实验报告

需要在新项目使用
我有 Reference

需要证明真实工程能力
我有 Core

需要准备面试
我有代码 + 实验 + 技术卡片 + 性能数据
```

这才是 JobPilot v2.0 的最终目标。
