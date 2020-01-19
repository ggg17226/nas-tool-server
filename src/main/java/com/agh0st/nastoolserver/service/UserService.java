package com.agh0st.nastoolserver.service;

import com.agh0st.nastoolserver.exception.PasswordIncorrectException;
import com.agh0st.nastoolserver.exception.SqlRuntimeException;
import com.agh0st.nastoolserver.exception.UserAlreadyExistException;
import com.agh0st.nastoolserver.exception.UserNotFoundException;
import com.agh0st.nastoolserver.mapper.UserMapper;
import com.agh0st.nastoolserver.object.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Random;
import java.util.UUID;

@Service
@Log4j2
public class UserService {

  @Resource UserMapper userMapper;

  private String generatePasswordHashString(@NotNull String password, @NotNull String salt) {
    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)) {
      return null;
    }
    return DigestUtils.md5Hex(DigestUtils.sha1Hex(password) + DigestUtils.md5Hex(salt))
        .toLowerCase();
  }

  /**
   * 获取用户信息
   *
   * @param username 用户名
   * @param password 密码
   * @return
   */
  public User getUserInfo(@NotNull String username, @NotNull String password)
      throws UserNotFoundException, PasswordIncorrectException {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new UserNotFoundException();
    }

    String passwordHash = generatePasswordHashString(password, userInfo.getSalt());

    if (userInfo.getPasswd().toLowerCase().equals(passwordHash)) {
      return userInfo;
    } else {
      throw new PasswordIncorrectException();
    }
  }

  /**
   * 获取用户信息
   *
   * @param username
   * @return
   */
  public User getUserInfo(@NotNull String username) throws UserNotFoundException {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new UserNotFoundException();
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
  @Transactional
  public boolean insertUser(@NotNull String username, @NotNull String password)
          throws SqlRuntimeException, UserAlreadyExistException {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new SqlRuntimeException("find_user");
    }
    if (userInfo != null && userInfo.getId() > 0) {
      throw new UserAlreadyExistException();
    }

    User user = new User();
    user.setUsername(username);
    user.setUuid(generateUUID());
    user.setSalt(this.generateSalt());
    user.setPasswd(generatePasswordHashString(password, user.getSalt()));
    try {
      return (userMapper.insertSelective(user) == 1);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new SqlRuntimeException("insert_user");
    }
  }

  @Transactional
  public boolean changePassword(
      @NotNull String username, @NotNull String oldPassword, @NotNull String newPassword) {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      userInfo = null;
    }
    if (userInfo != null) {
      return false;
    }
    String passwordHash = generatePasswordHashString(oldPassword, userInfo.getSalt());
    if (userInfo.getPasswd().toLowerCase().equals(passwordHash)) {
      User user = new User();
      user.setId(userInfo.getId());
      user.setSalt(generateSalt());
      user.setPasswd(generatePasswordHashString(newPassword, user.getSalt()));
      int i = userMapper.updateByPrimaryKeySelective(user);
      return i == 1;
    } else return false;
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
