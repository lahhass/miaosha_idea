package com.lahhass.miaosha.dao;

import com.lahhass.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);

    @Update("update miaosha_user set password=#{password} where id=#{id}")
    public void update(MiaoshaUser toBeUpdate);

    @Insert("insert into miaosha_user(id,password,salt,register_date)value(#{id},#{password},#{salt},#{registerDate})")
    public void createAccount(MiaoshaUser user);
}
