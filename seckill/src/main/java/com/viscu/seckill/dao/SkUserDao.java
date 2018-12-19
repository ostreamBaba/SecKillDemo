package com.viscu.seckill.dao;

import com.viscu.seckill.domain.SkUser;
import org.apache.ibatis.annotations.*;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Mapper
public interface SkUserDao {

    @Select("SELECT * FROM sk_user WHERE id = #{id}")
    SkUser getById(@Param("id") long id);

    @Update("UPDATE sk_user SET password=#{password} WHERE id=#{id}")
    void update(SkUser toBeUpdate);

    @Insert("INSERT INTO sk_user(id, nickname, password, salt, head, register_date, last_login_date, login_count)VALUES(#{id}, #{nickname},#{password},#{salt},#{head},#{registerDate},#{lastLoginDate},#{loginCount})")
    int insert(SkUser user);

}