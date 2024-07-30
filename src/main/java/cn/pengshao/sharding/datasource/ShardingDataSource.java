package cn.pengshao.sharding.datasource;

import cn.pengshao.sharding.engine.ShardingContext;
import cn.pengshao.sharding.config.ShardingProperties;
import cn.pengshao.sharding.engine.ShardingResult;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * sharding datasource.
 *
 * @Author: yezp
 * @date 2024/7/27 14:12
 */
@Slf4j
public class ShardingDataSource extends AbstractRoutingDataSource {

    public ShardingDataSource(ShardingProperties properties) {
        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        properties.getDataSources().forEach((key, value) -> {
            try {
                dataSourceMap.put(key, DruidDataSourceFactory.createDataSource(value));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        setTargetDataSources(dataSourceMap);
        // 默认数据源
        setDefaultTargetDataSource(dataSourceMap.values().iterator().next());
    }


    /**
     * 决定使用哪个数据源
     *
     * @return 数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        ShardingResult shardingResult = ShardingContext.get();
        Object key = shardingResult == null ? null : shardingResult.getTargetDataSourceName();
        log.info(" ===>> determineCurrentLookupKey:{}", key);
        return key;
    }
}
