package com.viscu.seckill.dao;

import com.viscu.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(@Param("id") int id);


    @Insert("INSERT INTO user(id, name) values(#{id}, #{name})")
    void insert(User user);

}
