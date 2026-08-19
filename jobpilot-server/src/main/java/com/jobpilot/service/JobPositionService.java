package com.jobpilot.service;

import com.jobpilot.dto.JobCreateDTO;
import com.jobpilot.dto.JobUpdateDTO;
import com.jobpilot.vo.JobVO;

import java.util.List;

/**
 * 职位服务（Phase 0.5 CRUD 样板）。
 *
 * <p>数据流：Controller → DTO → Service → Entity → Mapper → MySQL；
 * 返回：MySQL → Entity → Service → VO → Result&lt;T&gt; → HTTP。</p>
 */
public interface JobPositionService {

    /**
     * 创建职位：Snowflake 生成主键，createTime/updateTime 置当前时间，status 默认 1。
     */
    JobVO create(JobCreateDTO dto);

    /**
     * 按 ID 查询；不存在时抛出 {@link com.jobpilot.common.BusinessException}(NOT_FOUND)。
     */
    JobVO getById(Long id);

    /**
     * 职位列表（Phase 0.5 全量，分页优化属后续 Phase）。
     */
    List<JobVO> list();

    /**
     * 更新职位；不存在时抛出 {@link com.jobpilot.common.BusinessException}(NOT_FOUND)，
     * updateTime 刷新为当前时间。
     */
    JobVO update(Long id, JobUpdateDTO dto);
}
