package cn.pengshao.sharding.demo;

import cn.pengshao.sharding.ShardingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:39
 */
@SpringBootApplication
@Import({ShardingAutoConfiguration.class})
public class PsShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsShardingApplication.class, args);
    }

}
