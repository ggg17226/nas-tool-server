package com.agh0st.nastoolserver.service;

import com.agh0st.nastoolserver.exception.*;
import com.agh0st.nastoolserver.mapper.EmailCheckMapper;
import com.agh0st.nastoolserver.mapper.UserMapper;
import com.agh0st.nastoolserver.object.entity.EmailCheck;
import com.agh0st.nastoolserver.object.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class UserService {

  @Resource private UserMapper userMapper;
  @Resource private EmailCheckMapper emailCheckMapper;
  @Resource private StringRedisTemplate stringRedisTemplate;

  private static final int userMaxBindEmailPerDay = 20;

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
      throws UserNotFoundException, PasswordIncorrectException, SqlRuntimeException {
    User userInfo = checkAndGetUserInfo(username);

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
  public User getUserInfo(@NotNull String username)
      throws SqlRuntimeException, UserNotFoundException {
    User userInfo = checkAndGetUserInfo(username);
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
      throw new SqlRuntimeException("get_user_info");
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

  /**
   * 修改密码
   *
   * @param username
   * @param oldPassword
   * @param newPassword
   * @return
   * @throws SqlRuntimeException
   * @throws UserNotFoundException
   * @throws PasswordIncorrectException
   */
  @Transactional
  public boolean changePassword(
      @NotNull String username, @NotNull String oldPassword, @NotNull String newPassword)
      throws SqlRuntimeException, UserNotFoundException, PasswordIncorrectException {
    User userInfo = checkAndGetUserInfo(username);
    String passwordHash = generatePasswordHashString(oldPassword, userInfo.getSalt());
    if (userInfo.getPasswd().toLowerCase().equals(passwordHash)) {
      User user = new User();
      user.setId(userInfo.getId());
      user.setSalt(generateSalt());
      user.setPasswd(generatePasswordHashString(newPassword, user.getSalt()));
      int i = userMapper.updateByPrimaryKeySelective(user);
      return i == 1;
    } else throw new PasswordIncorrectException();
  }

  @Transactional
  public boolean bindEmail(@NotNull String username, @NotNull String email)
      throws SqlRuntimeException, UserNotFoundException, EmailCheckedException,
          TooQuickOperationException, TooManyOperationException {
    User userInfo = checkAndGetUserInfo(username);
    if (userInfo.getEmailChecked().intValue() == 1) {
      throw new EmailCheckedException();
    }
    if (Math.abs((System.currentTimeMillis() - getLastBindEmailTimestamp(username))) < 120000) {
      throw new TooQuickOperationException("bind_email");
    }
    setLastBindEmailTimestamp(username);
    if (!setAndCheckTodayBindEmailCount(username)) {
      throw new TooManyOperationException("bind_email");
    }

    EmailCheck emailCheck = new EmailCheck();
    emailCheck.setTargetEmail(email);
    emailCheck.setUid(userInfo.getId());
    emailCheck.setStatusCode(0);
    emailCheck.setCode(genRandomCode());
    int i = emailCheckMapper.insertSelective(emailCheck);
    return i == 1;
  }

  private boolean setAndCheckTodayBindEmailCount(@NotNull String username) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String todayBindEmailCountKey = username + "-" + df.format(new Date()) + "-bind_email";
    Boolean hasKey = stringRedisTemplate.hasKey(todayBindEmailCountKey);
    if (hasKey) {
      String val = stringRedisTemplate.opsForValue().get(todayBindEmailCountKey);
      int count = Integer.parseInt(val);
      count++;
      stringRedisTemplate
          .opsForValue()
          .set(todayBindEmailCountKey, Integer.toString(count), 2, TimeUnit.DAYS);
      return count <= userMaxBindEmailPerDay;
    } else {
      stringRedisTemplate.opsForValue().set(todayBindEmailCountKey, "1", 2, TimeUnit.DAYS);
      return true;
    }
  }

  /**
   * 设置用户最后一次绑定邮箱的时间
   *
   * @param username
   */
  private void setLastBindEmailTimestamp(@NotNull String username) {
    String lastBindEmailTimestampKey = username + "-last_bind_email";
    stringRedisTemplate
        .opsForValue()
        .set(
            lastBindEmailTimestampKey, Long.toString(System.currentTimeMillis()), 2, TimeUnit.DAYS);
  }

  /**
   * 获取用户最后一次绑定邮箱的时间
   *
   * @param username
   * @return
   */
  private long getLastBindEmailTimestamp(@NotNull String username) {
    String lastBindEmailTimestampKey = username + "-last_bind_email";
    Boolean hasKey = stringRedisTemplate.hasKey(lastBindEmailTimestampKey);
    if (!hasKey) {
      stringRedisTemplate.opsForValue().set(lastBindEmailTimestampKey, "0", 2, TimeUnit.DAYS);
      return 0;
    } else {
      String val = stringRedisTemplate.opsForValue().get(lastBindEmailTimestampKey);
      return Long.parseLong(val);
    }
  }

  /**
   * 检查并返回用户信息
   *
   * @param username
   * @return
   * @throws SqlRuntimeException
   * @throws UserNotFoundException
   */
  private User checkAndGetUserInfo(String username)
      throws SqlRuntimeException, UserNotFoundException {
    User userInfo = null;
    try {
      userInfo = userMapper.getUserInfoByUsername(username);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new SqlRuntimeException("get_user_info");
    }
    if (userInfo == null) {
      throw new UserNotFoundException();
    }
    return userInfo;
  }

  /**
   * 生成salt
   *
   * @return salt
   */
  private String generateSalt() {
    int start = (new SecureRandom()).nextInt(12);
    return DigestUtils.md5Hex(UUID.randomUUID().toString()).substring(start, start + 8);
  }

  /**
   * 生成用户uuid
   *
   * @return uuid
   */
  private String generateUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * 生成随机32位代码
   *
   * @return
   */
  private String genRandomCode() {
    return DigestUtils.md5Hex(UUID.randomUUID().toString());
  }
}
