package com.agh0st.nastoolserver.component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class HttpCode {
  @Data
  public static class CodeObject {
    private int code;
    private String desc;
    private String desc_cn;

    public CodeObject(int code, String desc, String desc_cn) {
      this.code = code;
      this.desc = desc;
      this.desc_cn = desc_cn;
    }
  }

  public static CodeObject SUCCESS = new CodeObject(0, "success", "执行成功");
  public static CodeObject DATA_ERROR = new CodeObject(1000, "data error", "数据错误");
  public static CodeObject USER_EXISTS = new CodeObject(1001, "User already exists", "用户已存在");
  public static CodeObject USERNAME_FORMAT_ERROR =
      new CodeObject(1002, "username format doesn't match", "用户名格式错误");
  public static CodeObject PASSWORD_FORMAT_ERROR =
      new CodeObject(1003, "password format doesn't match", "密码格式错误");
  public static CodeObject NO_SUCH_USER = new CodeObject(1004, "no such User", "无此用户");
  public static CodeObject USERNAME_OR_PASSWORD_ERROR =
      new CodeObject(1005, "username or password error", "用户名或密码错误");
  public static CodeObject ALREADY_LOGIN = new CodeObject(1006, "already logged in", "已经登陆");
  public static CodeObject CAPTCHA_ERROR = new CodeObject(1007, "captcha error", "验证码错误");
  public static CodeObject NEED_LOGIN = new CodeObject(1008, "need login", "尚未登陆");
  public static CodeObject SIGN_ERROR = new CodeObject(1009, "sign error", "签名错误");
  public static CodeObject TIMESTAMP_OUT = new CodeObject(1010, "timestamp out of range", "时间戳过期");
  public static CodeObject ALREADY_ACTIVATION = new CodeObject(1011, "already activation", "已经激活");
  public static CodeObject PERMISSION_DENIED = new CodeObject(40300, "permission denied", "无权访问");
  public static CodeObject METHOD_DENIED = new CodeObject(40301, "method denied", "无权使用此method");
  public static CodeObject METHOD_NOT_FOUND = new CodeObject(40400, "method not found", "404了啊亲");
  public static CodeObject DATA_DOES_NOT_EXIST =
      new CodeObject(40401, "Data does not exist", "数据不存在");
  public static CodeObject SYSTEM_ERROR = new CodeObject(50000, "system error", "系统错误");
  public static CodeObject DATABASE_ERROR = new CodeObject(50001, "database error", "数据库错误");
  public static CodeObject FILE_USING = new CodeObject(50002, "file using error", "文件使用中");
}
