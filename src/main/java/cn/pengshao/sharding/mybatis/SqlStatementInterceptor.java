package cn.pengshao.sharding.mybatis;

import cn.pengshao.sharding.engine.ShardingContext;
import cn.pengshao.sharding.engine.ShardingResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * intercept sql.
 *
 * @Author: yezp
 * @date 2024/7/27 14:20
 */
@Slf4j
@Component
@Intercepts(@Signature(type = StatementHandler.class,
        method = "prepare",
        args = {java.sql.Connection.class, Integer.class}))
public class SqlStatementInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ShardingResult result = ShardingContext.get();
        if (result != null) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            log.info(" ===> SqlStatementInterceptor: " + sql);
            String targetSqlStatement = result.getTargetSqlStatement();
            if (!sql.equalsIgnoreCase(targetSqlStatement)) {
                replaceSql(boundSql, targetSqlStatement);
            }
        }
        return invocation.proceed();
    }

    private void replaceSql(BoundSql boundSql, String sql) throws NoSuchFieldException {
        Field field = boundSql.getClass().getDeclaredField("sql");
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long fieldOffset = unsafe.objectFieldOffset(field);
        unsafe.putObject(boundSql, fieldOffset, sql);
    }
}
