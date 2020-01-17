package com.agh0st.nastoolserver.Mapper;

import com.agh0st.nastoolserver.object.PO.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.NotNull;

public interface UserMapper {
  int deleteByPrimaryKey(Integer id);

  int insert(User record);

  int insertSelective(User record);

  User selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(User record);

  int updateByPrimaryKey(User record);

  @ResultMap("com.agh0st.nastoolserver.Mapper.UserMapper.BaseResultMap")
  @Select("select * from user where `username`=#{username} limit 1")
  User getUserInfoByUsername(@NotNull @Param("username") String username);
}
