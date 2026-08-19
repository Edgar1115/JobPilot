package com.jobpilot.common;

/**
 * 统一错误码（规格书 v2.0 第 8 章）。
 *
 * <pre>
 * 0       SUCCESS
 * 400xx   参数/业务错误
 * 401xx   Token/认证错误
 * 403xx   权限错误
 * 404xx   资源不存在
 * 409xx   重复/状态冲突
 * 429xx   限流
 * 500xx   Server Error
 * 503xx   AI/MQ 等外部服务不可用
 * </pre>
 */
public enum ErrorCode {

    SUCCESS(0, "success"),

    BAD_REQUEST(40000, "参数错误"),
    VALIDATION_FAILED(40001, "参数校验失败"),

    UNAUTHORIZED(40100, "未认证"),
    TOKEN_EXPIRED(40101, "Token 已过期"),
    TOKEN_INVALID(40102, "Token 无效"),

    FORBIDDEN(40300, "无权限"),

    NOT_FOUND(40400, "资源不存在"),

    CONFLICT(40900, "重复/状态冲突"),

    RATE_LIMITED(42900, "请求过于频繁"),

    INTERNAL_ERROR(50000, "系统内部错误"),

    AI_SERVICE_UNAVAILABLE(50301, "AI 服务不可用"),
    MQ_SERVICE_UNAVAILABLE(50302, "消息服务不可用");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
