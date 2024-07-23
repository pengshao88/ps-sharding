package cn.pengshao.sharding.engine;

import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:47
 */
@Data
public class TableProperties {

    private List<String> actualDataNodes;
    private Properties databaseStrategy;
    private Properties tableStrategy;

}
