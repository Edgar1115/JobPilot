package com.jobpilot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职位视图对象（对外响应，禁止直接返回 Entity）。
 */
@Data
@Schema(description = "职位视图对象")
public class JobVO {

    @Schema(description = "职位 ID（Snowflake）", example = "3001")
    private Long id;

    @Schema(description = "所属用户 ID", example = "1001")
    private Long userId;

    @Schema(description = "公司名称", example = "字节跳动")
    private String companyName;

    @Schema(description = "职位名称", example = "后端开发工程师")
    private String positionName;

    @Schema(description = "职位描述 JD 文本")
    private String jdText;

    @Schema(description = "状态：1 有效 / 0 失效", example = "1")
    private Integer status;

    @Schema(description = "创建时间", example = "2026-08-19 13:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-08-19 13:00:00")
    private LocalDateTime updateTime;
}
