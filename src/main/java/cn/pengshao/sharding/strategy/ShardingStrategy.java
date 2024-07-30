package cn.pengshao.sharding.strategy;

import java.util.List;
import java.util.Map;

/**
 * strategy for sharding.
 *
 * @Author: yezp
 * @date 2024/7/30 22:26
 */
public interface ShardingStrategy {

    List<String> getShardingColumns();

    String doSharding(List<String> availableTargetNames, String logicTableName, Map<String, Object> shardingParams);

}
