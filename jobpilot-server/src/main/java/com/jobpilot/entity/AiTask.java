package com.jobpilot.entity;

import com.jobpilot.enums.AiTaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 异步任务（规格书 v2.0 第 16 章，表 ai_task）。
 *
 * <p>Phase 6 引入 RabbitMQ 生产消费，本阶段仅建表与 Entity。</p>
 */
@Data
public class AiTask {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 幂等请求 ID（唯一） */
    private String requestId;

    /** 所属用户 */
    private Long userId;

    /** 业务类型（如 RESUME_PARSE） */
    private String bizType;

    /** 业务对象 ID */
    private Long bizId;

    /** 任务状态（VARCHAR：PENDING / RUNNING / SUCCESS / FAILED / DEAD） */
    private AiTaskStatus status;

    /** 已重试次数，默认 0 */
    private Integer retryCount;

    /** 最大重试次数，默认 3 */
    private Integer maxRetry;

    /** 失败原因（可空） */
    private String errorMessage;

    /** 下次重试时间（可空） */
    private LocalDateTime nextRetryTime;

    /** 开始时间（可空） */
    private LocalDateTime startedAt;

    /** 结束时间（可空） */
    private LocalDateTime finishedAt;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
