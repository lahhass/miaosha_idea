package com.lahhass.miaosha.dao;

import com.lahhass.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Mapper
@Repository  //表明该类是用来执行与数据库相关的操作（即dao对象）
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id")int id);

    @Insert("insert into user(id,name)values(#{id},#{name})")
    public int insert(User user);
}
