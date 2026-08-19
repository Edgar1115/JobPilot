-- JobPilot — Phase 0.5 Persistence CRUD Foundation
-- 种子数据（只负责测试数据，建表见 schema.sql）
-- 主键使用规格书示例风格的可读 Snowflake 数值（1001/2001/3001...），便于手工验证。

-- sys_user（Phase 1 前仅作种子，密码为占位，未做任何编码）
INSERT INTO sys_user (id, username, email, password_hash, nickname, status, create_time, update_time) VALUES
    (1001, 'demo', 'demo@jobpilot.local', 'placeholder-hash-1001', '演示用户', 1, NOW(3), NOW(3)),
    (1002, 'alice', 'alice@jobpilot.local', 'placeholder-hash-1002', 'Alice', 1, NOW(3), NOW(3));

-- resume
INSERT INTO resume (id, user_id, name, file_url, raw_text, parsed_json, parse_status, create_time, update_time) VALUES
    (2001, 1001, '张三-后端简历.pdf', '/files/resume/2001.pdf', '张三，3 年 Java 后端经验……', '{"name":"张三","skills":["Java","Spring","MySQL"]}', 'SUCCESS', NOW(3), NOW(3)),
    (2002, 1002, 'Alice-Resume.pdf', '/files/resume/2002.pdf', NULL, NULL, 'PENDING', NOW(3), NOW(3));

-- job_position（Phase 0.5 CRUD 样板表的测试数据）
INSERT INTO job_position (id, user_id, company_name, position_name, jd_text, status, create_time, update_time) VALUES
    (3001, 1001, '字节跳动', '后端开发工程师', '负责核心业务系统设计与开发，熟悉 Java/Spring 技术栈，有高并发经验者优先。', 1, NOW(3), NOW(3)),
    (3002, 1001, '阿里巴巴', 'Java 高级工程师', '负责电商交易链路系统研发，要求精通 JVM、MySQL、Redis，具备性能调优能力。', 1, NOW(3), NOW(3)),
    (3003, 1002, '腾讯', '全栈开发工程师', '负责内部效率工具前后端开发，前端 Vue/React，后端 Java/Go。', 1, NOW(3), NOW(3));

-- interview_session
INSERT INTO interview_session (id, user_id, resume_id, job_id, title, status, current_round, max_round, started_at, finished_at, create_time, update_time) VALUES
    (4001, 1001, 2001, 3001, '字节跳动-后端开发工程师-模拟面试', 'CREATED', 0, 10, NULL, NULL, NOW(3), NOW(3));

-- interview_message
INSERT INTO interview_message (id, session_id, user_id, round_no, role, content, score, metadata, create_time) VALUES
    (5001, 4001, 1001, 0, 'SYSTEM', '欢迎参加模拟面试，本轮主题：Java 并发。', NULL, '{"round":0}', NOW(3)),
    (5002, 4001, 1001, 1, 'INTERVIEWER', '请介绍一下 synchronized 与 ReentrantLock 的区别。', NULL, '{"round":1}', NOW(3)),
    (5003, 4001, 1001, 1, 'USER', 'synchronized 是 JVM 层面的监视器锁……', 7.50, '{"round":1}', NOW(3));

-- interview_report
INSERT INTO interview_report (id, session_id, user_id, overall_score, java_score, database_score, redis_score, framework_score, project_score, summary, strengths, weaknesses, suggestions, raw_report_json, create_time, update_time) VALUES
    (6001, 4001, 1001, 7.50, 8.00, 7.00, 7.50, 8.00, 7.00, '整体表现良好，Java 基础扎实，并发理解有待深入。', '["Java 基础扎实","表达清晰"]', '["并发原理理解不深","Redis 场景经验少"]', '["多读《Java 并发编程实战》"]', '{"overall":7.5}', NOW(3), NOW(3));

-- ai_task
INSERT INTO ai_task (id, request_id, user_id, biz_type, biz_id, status, retry_count, max_retry, error_message, next_retry_time, started_at, finished_at, create_time, update_time) VALUES
    (7001, 'req-resume-2001-001', 1001, 'RESUME_PARSE', 2001, 'SUCCESS', 0, 3, NULL, NULL, NOW(3), NOW(3), NOW(3), NOW(3)),
    (7002, 'req-resume-2002-002', 1002, 'RESUME_PARSE', 2002, 'PENDING', 0, 3, NULL, NULL, NULL, NULL, NOW(3), NOW(3));

-- user_skill_profile
INSERT INTO user_skill_profile (id, user_id, skill_code, skill_name, score, sample_count, update_time) VALUES
    (8001, 1001, 'JAVA_CONCURRENT', 'Java 并发', 7.50, 3, NOW(3)),
    (8002, 1001, 'MYSQL', 'MySQL', 8.00, 5, NOW(3)),
    (8003, 1001, 'REDIS', 'Redis', 7.00, 2, NOW(3)),
    (8004, 1002, 'SPRING', 'Spring 框架', 8.50, 4, NOW(3));
