package com.agh0st.nastoolserver.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.agh0st.nastoolserver.object.PO.User;
import com.agh0st.nastoolserver.Mapper.base.UserBaseMapper;
/** @author aghost */
public interface UserMapper extends UserBaseMapper {

  public User getUserInfoByUsername(@Param(value = "username") String username);
}
