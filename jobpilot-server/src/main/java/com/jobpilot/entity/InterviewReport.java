package com.jobpilot.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 面试报告（规格书 v2.0 第 15 章，表 interview_report）。
 */
@Data
public class InterviewReport {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 关联会话（唯一） */
    private Long sessionId;

    /** 所属用户 */
    private Long userId;

    /** 综合评分（DECIMAL(5,2)） */
    private BigDecimal overallScore;

    private BigDecimal javaScore;

    private BigDecimal databaseScore;

    private BigDecimal redisScore;

    private BigDecimal frameworkScore;

    private BigDecimal projectScore;

    /** 总结（TEXT） */
    private String summary;

    /** 优势 JSON（当前阶段用 String 承载） */
    private String strengths;

    /** 不足 JSON（当前阶段用 String 承载） */
    private String weaknesses;

    /** 建议 JSON（当前阶段用 String 承载） */
    private String suggestions;

    /** AI 原始报告 JSON（当前阶段用 String 承载） */
    private String rawReportJson;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
