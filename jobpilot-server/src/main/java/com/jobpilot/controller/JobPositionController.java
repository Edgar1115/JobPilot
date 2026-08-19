package com.jobpilot.controller;

import com.jobpilot.common.Result;
import com.jobpilot.dto.JobCreateDTO;
import com.jobpilot.dto.JobUpdateDTO;
import com.jobpilot.service.JobPositionService;
import com.jobpilot.vo.JobVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位 CRUD 接口（规格书 v2.0 第 23 章 Job API；Phase 0.5 CRUD 样板）。
 *
 * <p>本阶段明确不实现：JWT、Ownership Check、Redis Cache、分页优化（均属后续 Phase）。</p>
 */
@Tag(name = "职位", description = "职位 CRUD（Phase 0.5 样板：POST / GET / GET{id} / PUT）")
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobPositionController {

    private final JobPositionService jobPositionService;

    @Operation(summary = "创建职位", description = "Snowflake 生成主键，status 默认 1，返回创建后的职位 VO。")
    @PostMapping
    public Result<JobVO> create(@Valid @RequestBody JobCreateDTO dto) {
        return Result.success(jobPositionService.create(dto));
    }

    @Operation(summary = "职位列表", description = "全量列表（Phase 0.5 不分页，分页优化属后续 Phase）。")
    @GetMapping
    public Result<List<JobVO>> list() {
        return Result.success(jobPositionService.list());
    }

    @Operation(summary = "职位详情", description = "按 ID 查询；不存在返回 code=40400。")
    @GetMapping("/{id}")
    public Result<JobVO> getById(@PathVariable("id") Long id) {
        return Result.success(jobPositionService.getById(id));
    }

    @Operation(summary = "更新职位", description = "PUT 全量更新可编辑字段，刷新 updateTime；不存在返回 code=40400。")
    @PutMapping("/{id}")
    public Result<JobVO> update(@PathVariable("id") Long id, @Valid @RequestBody JobUpdateDTO dto) {
        return Result.success(jobPositionService.update(id, dto));
    }
}
