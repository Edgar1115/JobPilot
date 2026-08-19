package com.jobpilot.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户（规格书 v2.0 第 10 章，表 sys_user）。
 */
@Data
public class SysUser {

    /** 主键：Snowflake BIGINT */
    private Long id;

    /** 用户名（唯一） */
    private String username;

    /** 邮箱（唯一，可空） */
    private String email;

    /** 密码哈希（Phase 1 引入编码方案） */
    private String passwordHash;

    /** 昵称（可空） */
    private String nickname;

    /** 状态：1 正常 / 0 禁用（TINYINT） */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
