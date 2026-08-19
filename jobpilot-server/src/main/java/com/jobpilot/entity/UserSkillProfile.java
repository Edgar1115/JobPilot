package com.jobpilot.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户技能画像（规格书 v2.0 第 17 章，表 user_skill_profile）。
 */
@Data
public class UserSkillProfile {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 所属用户 */
    private Long userId;

    /** 技能编码（如 JAVA_CONCURRENT / MYSQL / REDIS，用户内唯一） */
    private String skillCode;

    /** 技能名称 */
    private String skillName;

    /** 技能得分（DECIMAL(5,2)） */
    private BigDecimal score;

    /** 采样次数，默认 0 */
    private Integer sampleCount;

    private LocalDateTime updateTime;
}
