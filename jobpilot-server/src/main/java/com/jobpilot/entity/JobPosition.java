package com.jobpilot.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职位（规格书 v2.0 第 12 章，表 job_position）。
 *
 * <p>Phase 0.5 的 CRUD 样板表。</p>
 */
@Data
public class JobPosition {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 所属用户（Ownership Check 属后续 Phase） */
    private Long userId;

    /** 公司名称（可空） */
    private String companyName;

    /** 职位名称 */
    private String positionName;

    /** 职位描述 JD 文本 */
    private String jdText;

    /** 状态：1 有效 / 0 失效（TINYINT，默认 1） */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
