package com.jobpilot.service.impl;

import com.jobpilot.common.BusinessException;
import com.jobpilot.common.ErrorCode;
import com.jobpilot.common.SnowflakeIdGenerator;
import com.jobpilot.dto.JobCreateDTO;
import com.jobpilot.dto.JobUpdateDTO;
import com.jobpilot.entity.JobPosition;
import com.jobpilot.mapper.JobPositionMapper;
import com.jobpilot.service.JobPositionService;
import com.jobpilot.vo.JobVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 职位服务实现（Phase 0.5）。
 *
 * <p>职责：Snowflake 主键、createTime/updateTime、默认 status、
 * Entity / DTO / VO 转换、资源不存在判断（BusinessException + ErrorCode + 全局异常处理器）。</p>
 */
@Service
@RequiredArgsConstructor
public class JobPositionServiceImpl implements JobPositionService {

    private final JobPositionMapper jobPositionMapper;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public JobVO create(JobCreateDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        JobPosition entity = new JobPosition();
        entity.setId(snowflakeIdGenerator.nextId());
        entity.setUserId(dto.getUserId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setPositionName(dto.getPositionName());
        entity.setJdText(dto.getJdText());
        entity.setStatus(1); // 默认有效
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        jobPositionMapper.insert(entity);
        return toVO(entity);
    }

    @Override
    public JobVO getById(Long id) {
        return toVO(getEntityById(id));
    }

    @Override
    public List<JobVO> list() {
        return jobPositionMapper.selectList().stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public JobVO update(Long id, JobUpdateDTO dto) {
        JobPosition entity = getEntityById(id);

        entity.setCompanyName(dto.getCompanyName());
        entity.setPositionName(dto.getPositionName());
        entity.setJdText(dto.getJdText());
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        entity.setUpdateTime(LocalDateTime.now());

        jobPositionMapper.updateById(entity);
        return toVO(entity);
    }

    /** 查询实体；不存在时抛出统一业务异常（40400）。 */
    private JobPosition getEntityById(Long id) {
        JobPosition entity = jobPositionMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "职位不存在: " + id);
        }
        return entity;
    }

    private JobVO toVO(JobPosition entity) {
        JobVO vo = new JobVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setCompanyName(entity.getCompanyName());
        vo.setPositionName(entity.getPositionName());
        vo.setJdText(entity.getJdText());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }
}
