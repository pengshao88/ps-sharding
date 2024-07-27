package cn.pengshao.sharding.demo;

import cn.pengshao.sharding.ShardingAutoConfiguration;
import cn.pengshao.sharding.demo.mapper.UserMapper;
import cn.pengshao.sharding.demo.model.User;
import cn.pengshao.sharding.mybatis.ShardingMapperFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Description:
 *
 * @Author: yezp
 * @date 2024/7/23 22:39
 */
@SpringBootApplication
@Import({ShardingAutoConfiguration.class})
@MapperScan(value = "cn.pengshao.sharding.demo.mapper",
        factoryBean = ShardingMapperFactoryBean.class)
public class PsShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsShardingApplication.class, args);
    }

    @Autowired
    UserMapper userMapper;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            for (int id = 1; id <= 10; id++) {
                test(id);
            }
        };
    }

    private void test(int id) {
        System.out.println(" ===> 1. test insert ...");
        int inserted = userMapper.insert(new User(id, "pengshao", 18));
        System.out.println(" ===> inserted: " + inserted);

        System.out.println(" ===> 2. test find ...");
        User user = userMapper.findById(id);
        System.out.println(" ===> find: " + user);

        System.out.println(" ===> 3. test update ...");
        user.setName("PS");
        int updated = userMapper.update(user);
        System.out.println(" ===> updated: " + updated);

        System.out.println(" ===> 4. test find again ...");
        user = userMapper.findById(id);
        System.out.println(" ===> find again: " + user);
    }
}
