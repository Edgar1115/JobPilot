package com.jobpilot.enums;

/**
 * 简历解析状态（规格书 v2.0 第 11 章，resume.parse_status，VARCHAR）。
 *
 * <p>只包含规格书定义的状态，不新增。</p>
 */
public enum ResumeParseStatus {

    PENDING,
    PARSING,
    SUCCESS,
    FAILED
}
