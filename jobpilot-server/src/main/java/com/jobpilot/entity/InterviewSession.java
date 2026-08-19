package com.jobpilot.entity;

import com.jobpilot.enums.InterviewSessionStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模拟面试会话（规格书 v2.0 第 13 章，表 interview_session）。
 */
@Data
public class InterviewSession {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 所属用户 */
    private Long userId;

    /** 关联简历 */
    private Long resumeId;

    /** 关联职位 */
    private Long jobId;

    /** 会话标题（可空） */
    private String title;

    /** 会话状态（VARCHAR，见 InterviewSessionStatus 状态机） */
    private InterviewSessionStatus status;

    /** 当前轮次，默认 0 */
    private Integer currentRound;

    /** 最大轮次，默认 10 */
    private Integer maxRound;

    /** 开始时间（可空） */
    private LocalDateTime startedAt;

    /** 结束时间（可空） */
    private LocalDateTime finishedAt;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
