package cn.pengshao.sharding.engine;

import lombok.Data;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:45
 */
@Data
public class ShardingResult {

    private String targetDataSourceName;
    private String targetSqlString;
    private Object[] parameters;

}
