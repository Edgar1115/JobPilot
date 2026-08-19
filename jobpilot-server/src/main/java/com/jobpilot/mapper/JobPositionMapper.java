package com.jobpilot.mapper;

import com.jobpilot.entity.JobPosition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 职位 Mapper（规格书 v2.0 第 12 章；Phase 0.5 CRUD 样板）。
 *
 * <p>接口由 {@code @MapperScan("com.jobpilot.mapper")} 扫描注册；
 * SQL 全部在 {@code resources/mapper/JobPositionMapper.xml}（禁止 SELECT * / 字符串拼接）。</p>
 *
 * <p>deleteById 虽然当前 HTTP API 不暴露，但 Mapper 必须具备，
 * 供 CRUD Integration Test 验证完整的 C / R / U / D 链路。</p>
 */
@Mapper
public interface JobPositionMapper {

    int insert(JobPosition jobPosition);

    JobPosition selectById(Long id);

    List<JobPosition> selectList();

    int updateById(JobPosition jobPosition);

    int deleteById(Long id);
}
