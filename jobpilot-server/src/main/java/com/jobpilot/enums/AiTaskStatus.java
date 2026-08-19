package com.jobpilot.enums;

/**
 * AI 异步任务状态（规格书 v2.0 第 16 章，ai_task.status，VARCHAR）。
 *
 * <p>只包含规格书定义的状态，不新增。</p>
 */
public enum AiTaskStatus {

    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    DEAD
}
