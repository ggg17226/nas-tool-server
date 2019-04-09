package com.agh0st.nastoolserver.service;

import com.agh0st.nastoolserver.Mapper.UserMapper;
import com.agh0st.nastoolserver.object.PO.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.logging.log4j.core.util.Integers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserService {

  @Resource UserMapper userMapper;

  public User checkUserLogin(String username, String password) {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception ex) {
      userInfo = null;
    }
    if (userInfo == null) return null;
    return userInfo;
  }

  //  public boolean insertUser(String username, String password) {
  //    User user = new User();
  //    user.setUsername(username);
  //    user.setUuid(generateUUID());
  //  }

  public String generateSalt() {
    return Md5Crypt.md5Crypt(UUID.randomUUID().toString().getBytes()).substring(0, 8);
  }

  private String generateUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
