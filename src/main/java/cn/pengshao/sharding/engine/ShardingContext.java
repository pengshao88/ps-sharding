package cn.pengshao.sharding.engine;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/27 14:07
 */
public class ShardingContext {

    private static final ThreadLocal<ShardingResult> LOCAL = new ThreadLocal<>();

    public static ShardingResult get() {
        return LOCAL.get();
    }

    public static void set(ShardingResult shardingResult) {
        LOCAL.set(shardingResult);
    }

}
