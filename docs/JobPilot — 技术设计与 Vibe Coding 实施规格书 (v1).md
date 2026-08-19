# JobPilot — AI 智能求职与模拟面试平台

## 技术设计与 Vibe Coding 实施规格书

> **项目定位：** Java 后端面试导向项目  
> **核心原则：** 以真实可运行系统为基础，用最少业务代码覆盖最多 Java 后端高频技术面知识点。  
> **开发模式：** Java 单体主服务 + Python AI 服务 + MySQL + Redis + RabbitMQ。  
> **目标：** 最终做到简历上的每一个主要技术点，都有真实代码、真实业务场景和可演示结果。

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

```text
jobpilot/
│
├── jobpilot-server/
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
├── jobpilot-ai/
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
├── deploy/
│   └── docker-compose.yml
│
├── docs/
│
└── README.md
```

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

# 64. Vibe Coding Phase

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

JobPilot 的开发目标不是做一个理论上的“大型互联网系统”。

真正目标是：

> **做一个能够真实运行、能够真实压测、能够现场展示代码，并且简历上每个核心技术点都经得起面试官连续追问 3～5 层的 Java 后端项目。**

任何新增技术，都必须能够回答两个问题：

1. **这个项目具体出现了什么问题？**
2. **为什么这个技术比更简单的方案更合适？**

回答不了，就不要加入。