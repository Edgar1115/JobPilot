package com.jobpilot.entity;

import com.jobpilot.enums.InterviewRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 面试消息（规格书 v2.0 第 14 章，表 interview_message）。
 */
@Data
public class InterviewMessage {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 所属会话 */
    private Long sessionId;

    /** 所属用户 */
    private Long userId;

    /** 轮次编号 */
    private Integer roundNo;

    /** 消息角色（VARCHAR：SYSTEM / INTERVIEWER / USER） */
    private InterviewRole role;

    /** 消息内容（MEDIUMTEXT） */
    private String content;

    /** 评分（DECIMAL(5,2)，可空） */
    private BigDecimal score;

    /** 元数据 JSON（当前阶段用 String 承载，可空） */
    private String metadata;

    private LocalDateTime createTime;
}
