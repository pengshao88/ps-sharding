package cn.pengshao.sharding.demo.mapper;

import cn.pengshao.sharding.demo.model.User;
import org.apache.ibatis.annotations.*;

/**
 * user mapper.
 *
 * @Author: yezp
 * @date 2024/7/23 22:43
 */
@Mapper
public interface UserMapper {

    @Insert("insert into t_user (id, name, age) values (#{id}, #{name}, #{age})")
    int insert(User user);

    @Select("select * from t_user where id = #{id}")
    User findById(int id);

    @Update("update t_user set name = #{name}, age = #{age} where id = #{id}")
    int update(User user);

    @Delete("delete from t_user where id = #{id}")
    int delete(int id);
}
