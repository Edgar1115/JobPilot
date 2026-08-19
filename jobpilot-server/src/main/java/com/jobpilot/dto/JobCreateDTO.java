package com.jobpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建职位请求（POST /api/v1/jobs）。
 *
 * <p>Phase 0.5 无登录态，userId 由调用方显式传入；JWT / Ownership Check 属后续 Phase。</p>
 */
@Data
@Schema(description = "创建职位请求")
public class JobCreateDTO {

    @NotNull(message = "userId 不能为空")
    @Schema(description = "所属用户 ID（Phase 0.5 无登录态，显式传入）", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Size(max = 100, message = "companyName 长度不能超过 100")
    @Schema(description = "公司名称（可空）", example = "字节跳动")
    private String companyName;

    @NotBlank(message = "positionName 不能为空")
    @Size(max = 100, message = "positionName 长度不能超过 100")
    @Schema(description = "职位名称", example = "后端开发工程师", requiredMode = Schema.RequiredMode.REQUIRED)
    private String positionName;

    @NotBlank(message = "jdText 不能为空")
    @Schema(description = "职位描述 JD 文本", example = "负责核心业务系统设计与开发，熟悉 Java/Spring 技术栈。", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jdText;
}
