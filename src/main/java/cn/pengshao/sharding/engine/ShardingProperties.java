package cn.pengshao.sharding.engine;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:48
 */
@Data
@ConfigurationProperties(prefix = "spring.sharding")
public class ShardingProperties {

    private Map<String, Properties> dataSources = new LinkedHashMap<>();
    private Map<String, TableProperties> tables = Collections.emptyMap();

}
