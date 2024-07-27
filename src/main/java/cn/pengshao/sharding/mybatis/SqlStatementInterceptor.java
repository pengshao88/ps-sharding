package cn.pengshao.sharding.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

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
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        log.info(" ===> sql statement:{}", boundSql.getSql());

        Object parameterObject = boundSql.getParameterObject();
        log.info(" ===> sql parameters:{}", parameterObject);
        // todo 修改sql user -> user_1
        return invocation.proceed();
    }
}
