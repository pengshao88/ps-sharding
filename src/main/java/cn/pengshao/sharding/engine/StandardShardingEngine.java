package cn.pengshao.sharding.engine;

import cn.pengshao.sharding.config.ShardingProperties;
import cn.pengshao.sharding.strategy.HashShardingStrategy;
import cn.pengshao.sharding.strategy.ShardingStrategy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/30 22:39
 */
@Slf4j
public class StandardShardingEngine implements ShardingEngine {

    private final Map<String, ShardingStrategy> databaseStrategies = new HashMap<>();
    private final Map<String, ShardingStrategy> tableStrategies = new HashMap<>();
    private final MultiValueMap<String, String> actualDatabaseNames = new LinkedMultiValueMap<>();
    private final MultiValueMap<String, String> actualTableNames = new LinkedMultiValueMap<>();

    public StandardShardingEngine(ShardingProperties properties) {
        properties.getTables().forEach((table, tableProperties) -> {
            tableProperties.getActualDataNodes().forEach((actualDataNode) -> {
                String[] split = actualDataNode.split("\\.");
                String databaseName = split[0], tableName = split[1];
                actualDatabaseNames.add(databaseName, tableName);
                actualTableNames.add(tableName, databaseName);
            });

            databaseStrategies.put(table, new HashShardingStrategy(tableProperties.getDatabaseStrategy()));
            tableStrategies.put(table, new HashShardingStrategy(tableProperties.getTableStrategy()));
        });
    }

    @Override
    public ShardingResult sharding(String sql, Object[] args) {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        String table;
        Map<String, Object> shardingColumnsMap;

        // insert
        if (sqlStatement instanceof SQLInsertStatement sqlInsertStatement) {
            table = sqlInsertStatement.getTableName().getSimpleName();
            shardingColumnsMap = new HashMap<>();
            List<SQLExpr> columns = sqlInsertStatement.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                SQLExpr column = columns.get(i);
                SQLIdentifierExpr columnExpr = (SQLIdentifierExpr) columns.get(i);
                String columnName = columnExpr.getSimpleName();
                // column : value
                shardingColumnsMap.put(columnName, args[i]);
            }
        } else {
            // select/update/delete
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            visitor.setParameters(List.of(args));
            sqlStatement.accept(visitor);

            LinkedHashSet<SQLName> sqlNames = new LinkedHashSet<>(visitor.getOriginalTables());
            if (sqlNames.size() > 1) {
                throw new RuntimeException("not support multi tables sharding: " + sqlNames);
            }
            table = sqlNames.iterator().next().getSimpleName();
            log.info(" ===>>> visitor.getOriginalTables:{}", table);
            shardingColumnsMap = visitor.getConditions().stream()
                    .collect(Collectors.toMap(c -> c.getColumn().getName(), c -> c.getValues().get(0)));
            log.info(" ===>>> visitor.getConditions:{}", shardingColumnsMap);
        }

        ShardingStrategy databaseStrategy = databaseStrategies.get(table);
        String targetDatabase = databaseStrategy.doSharding(actualDatabaseNames.get(table), table, shardingColumnsMap);
        ShardingStrategy tableStrategy = tableStrategies.get(table);
        String targetTable = tableStrategy.doSharding(actualTableNames.get(table), table, shardingColumnsMap);
        log.info(" ===>>> ");
        log.info(" ===>>> target db.table = " + targetDatabase + "." + targetTable);
        log.info(" ===>>> ");
        return new ShardingResult(targetDatabase, sql.replace(table, targetTable));
    }
}
