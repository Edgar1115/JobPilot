package com.jobpilot.enums;

/**
 * 模拟面试会话状态（规格书 v2.0 第 13 章，interview_session.status，VARCHAR）。
 *
 * <p>状态机：CREATED → ACTIVE → FINISHED → REPORT_PENDING → REPORTING → COMPLETED；
 * CREATED / ACTIVE → ABORTED；REPORTING 失败回退 REPORT_PENDING。</p>
 *
 * <p>只包含规格书定义的状态，不新增。</p>
 */
public enum InterviewSessionStatus {

    CREATED,
    ACTIVE,
    FINISHED,
    REPORT_PENDING,
    REPORTING,
    COMPLETED,
    ABORTED
}
