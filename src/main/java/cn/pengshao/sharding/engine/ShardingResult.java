package cn.pengshao.sharding.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardingResult {

    private String targetDataSourceName;
    private String targetSqlStatement;
//    private Object[] parameters;

}
