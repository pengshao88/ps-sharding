package cn.pengshao.sharding.engine;

import cn.pengshao.sharding.config.ShardingProperties;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/30 22:39
 */
public class StandardShardingEngine implements ShardingEngine {

    public StandardShardingEngine(ShardingProperties properties) {

    }

    @Override
    public ShardingResult sharding(String sql, Object[] args) {
        return null;
    }
}
