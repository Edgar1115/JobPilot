package com.jobpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新职位请求（PUT /api/v1/jobs/{id}）。
 *
 * <p>PUT 语义：positionName / jdText 必填；companyName 可空；status 不传则保持原值。
 * userId 与 id 不可修改（Ownership Check 属后续 Phase）。</p>
 */
@Data
@Schema(description = "更新职位请求")
public class JobUpdateDTO {

    @Size(max = 100, message = "companyName 长度不能超过 100")
    @Schema(description = "公司名称（可空）", example = "字节跳动-抖音电商")
    private String companyName;

    @NotBlank(message = "positionName 不能为空")
    @Size(max = 100, message = "positionName 长度不能超过 100")
    @Schema(description = "职位名称", example = "资深后端开发工程师", requiredMode = Schema.RequiredMode.REQUIRED)
    private String positionName;

    @NotBlank(message = "jdText 不能为空")
    @Schema(description = "职位描述 JD 文本", example = "负责核心业务系统设计与开发，要求熟悉 Java/Spring、MySQL、Redis。", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jdText;

    @Min(value = 0, message = "status 只能为 0 或 1")
    @Max(value = 1, message = "status 只能为 0 或 1")
    @Schema(description = "状态：1 有效 / 0 失效（不传则保持原值）", example = "1")
    private Integer status;
}
