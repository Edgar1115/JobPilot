package com.jobpilot.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一响应（规格书 v2.0 第 8 章）。
 *
 * <pre>
 * {
 *   "code": 0,
 *   "message": "success",
 *   "data": {},
 *   "requestId": "xxx"
 * }
 * </pre>
 */
@Schema(description = "统一响应包装：所有业务接口均以 Result 返回（code=0 表示成功）")
public class Result<T> {

    @Schema(description = "业务错误码，0 表示成功；其余见错误码范围（400xx/401xx/403xx/404xx/409xx/429xx/500xx/503xx）", example = "0")
    private Integer code;

    @Schema(description = "提示信息，成功为 success，失败为具体错误描述", example = "success")
    private String message;

    @Schema(description = "业务数据，随接口不同而不同；失败时为 null")
    private T data;

    @Schema(description = "请求追踪 ID，用于日志串联排查（Phase 1 RequestIdFilter 填充）", example = "7f3c1a2b-8d4e-4f6a-9c0d-1e2f3a4b5c6d")
    private String requestId;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.requestId = currentRequestId();
    }

    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isSuccess() {
        return ErrorCode.SUCCESS.getCode().equals(code);
    }

    private String currentRequestId() {
        return org.slf4j.MDC.get("requestId");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
