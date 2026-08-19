-- JobPilot — Phase 0.5 Persistence CRUD Foundation
-- 数据库 DDL（只负责建表，测试数据见 seed.sql）
-- 依据：规格书 v2.0 第 9~17 章，字段名称/类型/索引/约束与规格书完全一致
-- 字符集：utf8mb4；主键：BIGINT（业务侧 Snowflake 生成，不使用 AUTO_INCREMENT）
--
-- 执行方式（一次性，对运行中的 MySQL 容器）：
--   docker exec -i jobpilot-mysql mysql -ujobpilot -pjobpilot123 jobpilot < deploy/mysql/schema.sql
--   docker exec -i jobpilot-mysql mysql -ujobpilot -pjobpilot123 jobpilot < deploy/mysql/seed.sql

-- ---------------------------------------------------------------
-- 10. sys_user（规格书 v2.0 第 10 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 11. resume（规格书 v2.0 第 11 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 12. job_position（规格书 v2.0 第 12 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 13. interview_session（规格书 v2.0 第 13 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 14. interview_message（规格书 v2.0 第 14 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 15. interview_report（规格书 v2.0 第 15 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 16. ai_task（规格书 v2.0 第 16 章）
-- ---------------------------------------------------------------
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

-- ---------------------------------------------------------------
-- 17. user_skill_profile（规格书 v2.0 第 17 章）
-- ---------------------------------------------------------------
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
