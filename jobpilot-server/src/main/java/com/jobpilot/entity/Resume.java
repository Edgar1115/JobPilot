package com.jobpilot.entity;

import com.jobpilot.enums.ResumeParseStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历（规格书 v2.0 第 11 章，表 resume）。
 */
@Data
public class Resume {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 所属用户 */
    private Long userId;

    /** 简历名称 */
    private String name;

    /** 文件 URL（可空） */
    private String fileUrl;

    /** 解析出的原始文本（MEDIUMTEXT，可空） */
    private String rawText;

    /** 解析结果 JSON（当前阶段用 String 承载） */
    private String parsedJson;

    /** 解析状态（VARCHAR，默认 PENDING） */
    private ResumeParseStatus parseStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
