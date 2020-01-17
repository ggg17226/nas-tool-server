package com.agh0st.nastoolserver.service;

import com.agh0st.nastoolserver.Mapper.UserMapper;
import com.agh0st.nastoolserver.object.PO.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

  @Resource UserMapper userMapper;

  /**
   * 获取用户信息
   *
   * @param username 用户名
   * @param password 密码
   * @return
   */
  public User getUserInfo(String username, String password) {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception ex) {
      userInfo = null;
    }
    if (userInfo == null) return null;

    String passwordHash =
        DigestUtils.md5Hex(DigestUtils.sha1Hex(password) + DigestUtils.md5Hex(userInfo.getSalt()))
            .toLowerCase();

    return userInfo.getPasswd().toLowerCase().equals(passwordHash) ? userInfo : null;
  }

  /**
   * 获取用户信息
   *
   * @param username
   * @return
   */
  public User getUserInfo(String username) {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception ex) {
      userInfo = null;
    }
    return userInfo;
  }

  /**
   * 新建用户
   *
   * @param username 用户名
   * @param password 密码
   * @return 是否成功
   */
  public boolean insertUser(String username, String password) {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception ex) {
      userInfo = null;
    }
    if (userInfo != null) {
      return false;
    }
    User user = new User();
    user.setUsername(username);
    user.setUuid(generateUUID());
    user.setSalt(this.generateSalt());
    user.setPasswd(
        DigestUtils.md5Hex(DigestUtils.sha1Hex(password) + DigestUtils.md5Hex(user.getSalt())));
    try {
      return (userMapper.insertSelective(user) == 1);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 生成salt
   *
   * @return salt
   */
  private String generateSalt() {
    int start = (new Random()).nextInt(12);
    return DigestUtils.md5Hex(UUID.randomUUID().toString().getBytes()).substring(start, start + 8);
  }

  /**
   * 生成用户uuid
   *
   * @return uuid
   */
  private String generateUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
