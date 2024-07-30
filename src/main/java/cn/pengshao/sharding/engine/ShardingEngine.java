package cn.pengshao.sharding.engine;

/**
 * Core sharding engine.
 *
 * @Author: yezp
 * @date 2024/7/30 22:22
 */
public interface ShardingEngine {

    ShardingResult sharding(String sql, Object[] args);

}
