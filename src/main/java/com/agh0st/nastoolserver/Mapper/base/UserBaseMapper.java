package com.agh0st.nastoolserver.Mapper.base;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.agh0st.nastoolserver.object.PO.User;
/** @author aghost */
public interface UserBaseMapper {

  int insertUser(User object);

  int updateUser(User object);

  int update(User.UpdateBuilder object);

  List<User> queryUser(User object);

  User queryUserLimit1(User object);
}
