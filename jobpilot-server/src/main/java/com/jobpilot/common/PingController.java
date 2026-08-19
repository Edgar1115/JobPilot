package com.jobpilot.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Phase 0 骨架验证接口：
 * 验证统一 Result 与全局异常处理链路是否生效（规格书第 8 / 56 章）。
 * 后续 Phase 业务接口遵循同样的返回约定。
 */
@Tag(name = "系统", description = "系统健康检查与骨架验证接口（Phase 0）")
@RestController
@RequestMapping("/api/v1")
public class PingController {

    @Operation(summary = "服务探活",
            description = "返回 pong，验证统一 Result 响应与请求链路是否正常。")
    @ApiResponse(responseCode = "200", description = "成功，data 为 pong")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("pong");
    }

    @Operation(summary = "骨架异常验证",
            description = "主动抛出 BusinessException(50000)，验证全局异常处理链路（规格书第 56 章）："
                    + "内部记录完整堆栈，对外只返回统一错误码，不暴露实现细节。")
    @ApiResponse(responseCode = "200", description = "统一错误响应，code=50000（全局异常生效）")
    @GetMapping("/ping/error")
    public Result<Void> pingError() {
        throw new BusinessException(ErrorCode.INTERNAL_ERROR, "skeleton validation exception");
    }
}
