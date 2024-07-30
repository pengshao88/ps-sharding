package cn.pengshao.sharding.config;

import cn.pengshao.sharding.datasource.ShardingDataSource;
import cn.pengshao.sharding.config.ShardingProperties;
import cn.pengshao.sharding.engine.ShardingEngine;
import cn.pengshao.sharding.engine.StandardShardingEngine;
import cn.pengshao.sharding.mybatis.SqlStatementInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sharding auto configuration.
 *
 * @Author: yezp
 * @date 2024/7/23 22:35
 */
@Configuration
@EnableConfigurationProperties(ShardingProperties.class)
public class ShardingAutoConfiguration {

    @Bean
    public ShardingDataSource shardingDataSource(ShardingProperties properties) {
        return new ShardingDataSource(properties);
    }

    @Bean
    public ShardingEngine shardingEngine(ShardingProperties properties) {
        return new StandardShardingEngine(properties);
    }

    @Bean
    public SqlStatementInterceptor sqlStatementInterceptor() {
        return new SqlStatementInterceptor();
    }
}
