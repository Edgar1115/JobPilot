package com.jobpilot.mapper;

import com.jobpilot.common.SnowflakeIdGenerator;
import com.jobpilot.entity.JobPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 真实 MySQL CRUD Integration Test（Phase 0.5 验收项之一）。
 *
 * <p>连接本地 docker MySQL（application.yml 默认配置），完整验证：
 * INSERT → SELECT → UPDATE → DELETE → 再次 SELECT 确认不存在。</p>
 *
 * <p>防污染：测试数据使用专用标识（user_id=999999999、positionName 前缀 TEST_CRUD_），
 * 末尾删除；{@link #cleanup()} 在断言中断时兜底删除，确保测试后不残留正式数据。</p>
 */
@SpringBootTest
class JobPositionMapperIT {

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    /** 测试专用 user_id，避免与 seed 数据（1001/1002）混淆 */
    private static final long TEST_USER_ID = 999999999L;

    private Long createdId;

    @AfterEach
    void cleanup() {
        if (createdId != null) {
            jobPositionMapper.deleteById(createdId);
            createdId = null;
        }
    }

    @Test
    void crudRoundTrip_insertSelectUpdateDelete() {
        // ---------- INSERT ----------
        JobPosition entity = new JobPosition();
        entity.setId(snowflakeIdGenerator.nextId());
        entity.setUserId(TEST_USER_ID);
        entity.setCompanyName("TestCompany");
        entity.setPositionName("TEST_CRUD_" + entity.getId());
        entity.setJdText("Integration test JD text");
        entity.setStatus(1);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        int inserted = jobPositionMapper.insert(entity);
        assertThat(inserted).isEqualTo(1);
        createdId = entity.getId();

        // ---------- SELECT ----------
        JobPosition fetched = jobPositionMapper.selectById(createdId);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(createdId);
        assertThat(fetched.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(fetched.getCompanyName()).isEqualTo("TestCompany");
        assertThat(fetched.getPositionName()).isEqualTo(entity.getPositionName());
        assertThat(fetched.getJdText()).isEqualTo("Integration test JD text");
        assertThat(fetched.getStatus()).isEqualTo(1);
        assertThat(fetched.getCreateTime()).isNotNull();
        assertThat(fetched.getUpdateTime()).isNotNull();
        // snake_case → camelCase 自动映射验证
        assertThat(fetched.getCompanyName()).isEqualTo(entity.getCompanyName());
        assertThat(fetched.getPositionName()).isEqualTo(entity.getPositionName());

        // ---------- UPDATE ----------
        fetched.setPositionName("TEST_CRUD_UPDATED_" + createdId);
        fetched.setJdText("Updated JD text");
        fetched.setStatus(0);
        fetched.setUpdateTime(LocalDateTime.now());
        int updated = jobPositionMapper.updateById(fetched);
        assertThat(updated).isEqualTo(1);

        JobPosition afterUpdate = jobPositionMapper.selectById(createdId);
        assertThat(afterUpdate).isNotNull();
        assertThat(afterUpdate.getPositionName()).isEqualTo("TEST_CRUD_UPDATED_" + createdId);
        assertThat(afterUpdate.getJdText()).isEqualTo("Updated JD text");
        assertThat(afterUpdate.getStatus()).isEqualTo(0);

        // ---------- DELETE ----------
        int deleted = jobPositionMapper.deleteById(createdId);
        assertThat(deleted).isEqualTo(1);

        // ---------- 再次 SELECT 确认不存在 ----------
        assertThat(jobPositionMapper.selectById(createdId)).isNull();
        createdId = null;
    }
}
