package com.jobpilot.common;

import org.springframework.stereotype.Component;

/**
 * 雪花 ID 生成器（规格书 v2.0 第 18 章）。
 *
 * <p>业务主键采用 Snowflake BIGINT；MVP 阶段实现最小单 JVM 版本：
 * 固定 datacenterId=0 / workerId=1（不做分布式注册、不依赖 Redis INCR、不建独立 ID Service），
 * 保证开发环境单 JVM 下 CRUD 主键唯一且递增趋势。</p>
 *
 * <pre>
 * | 1 bit 符号 | 41 bit 毫秒时间戳 | 5 bit datacenterId | 5 bit workerId | 12 bit 序列号 |
 * </pre>
 *
 * <p>同一毫秒内序列号溢出时自旋等待下一毫秒；时钟回拨时等待追上（单 JVM 足够）。</p>
 */
@Component
public class SnowflakeIdGenerator {

    /** 起始时间戳：2024-01-01 00:00:00 UTC */
    private static final long EPOCH = 1704067200000L;

    private static final long DATACENTER_ID_BITS = 5L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS); // 31
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);         // 31
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);          // 4095

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private final long datacenterId;
    private final long workerId;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator() {
        this(0L, 1L);
    }

    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId 超出范围 [0, " + MAX_DATACENTER_ID + "]");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("workerId 超出范围 [0, " + MAX_WORKER_ID + "]");
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * 生成下一个 BIGINT 主键（线程安全）。
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 时钟回拨：等待追上最后生成时刻
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset > 5L) {
                throw new IllegalStateException("时钟回拨超过 5ms，拒绝生成：offset=" + offset);
            }
            try {
                Thread.sleep(offset);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("时钟回拨等待被中断", e);
            }
            timestamp = System.currentTimeMillis();
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
