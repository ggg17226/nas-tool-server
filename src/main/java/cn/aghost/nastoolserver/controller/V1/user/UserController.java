package cn.aghost.nastoolserver.controller.V1.user;

import cn.aghost.nastoolserver.aop.interfaces.CheckUserInfo;
import cn.aghost.nastoolserver.exception.*;
import cn.aghost.nastoolserver.object.entity.User;
import cn.aghost.nastoolserver.object.request.BindEmailRequest;
import cn.aghost.nastoolserver.object.request.ChangePassowrdRequest;
import cn.aghost.nastoolserver.object.request.LoginRequest;
import cn.aghost.nastoolserver.object.response.HttpCode;
import cn.aghost.nastoolserver.object.response.HttpDataResponse;
import cn.aghost.nastoolserver.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/V1/user")
@Slf4j
public class UserController {
  private static final String USER_CAPTCHA_CODE_KEY = "user-captcha-code";
  private static final String USER_CAPTCHA_GEN_TIME_KEY = "user-captcha-gen-time";
  @Resource private UserService userService;
  @Resource private Producer captchaProducer;

  @Value("${app.regFlag}")
  private boolean regFlag;

  /**
   * 检查提交数据
   *
   * @param loginRequest
   * @return
   */
  private boolean checkUserUploadData(LoginRequest loginRequest) {
    return loginRequest == null
        || StringUtils.isEmpty(loginRequest.getUsername())
        || StringUtils.isEmpty(loginRequest.getPassword())
        || StringUtils.isEmpty(loginRequest.getCaptcha());
  }

  /**
   * 清理验证码session
   *
   * @param session
   */
  private void clearCaptchaSession(HttpSession session) {
    try {
      session.removeAttribute(USER_CAPTCHA_GEN_TIME_KEY);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    try {
      session.removeAttribute(USER_CAPTCHA_CODE_KEY);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * 校验验证码
   *
   * @param session
   * @param captchaCode
   * @return
   */
  private boolean checkCaptchaCode(HttpSession session, String captchaCode) {
    String code, time;
    captchaCode = captchaCode.trim();
    try {
      code = session.getAttribute(USER_CAPTCHA_CODE_KEY).toString();
      time = session.getAttribute(USER_CAPTCHA_GEN_TIME_KEY).toString();
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      clearCaptchaSession(session);
      return false;
    }
    if (code == null || time == null || StringUtils.isEmpty(code) || StringUtils.isEmpty(time)) {
      clearCaptchaSession(session);
      return false;
    }
    long genTime;
    try {
      genTime = Long.parseLong(time);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      clearCaptchaSession(session);
      return false;
    }
    if (Math.abs(System.currentTimeMillis() - genTime) > 120000) {
      clearCaptchaSession(session);
      return false;
    }
    if (code.trim().toLowerCase().equals(captchaCode.trim().toLowerCase())) {
      clearCaptchaSession(session);
      return true;
    } else {
      return false;
    }
  }

  @GetMapping(
      value = {"/checkBind/{code}"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object checkBind(@PathVariable @NotNull String code) {
    if (StringUtils.isBlank(code) || StringUtils.length(code.trim()) != 32) {
      return new HttpDataResponse(HttpCode.DATA_ERROR);
    }
    return userService.verifyBindEmail(code)
        ? new HttpDataResponse(HttpCode.SUCCESS)
        : new HttpDataResponse(HttpCode.DATA_ERROR);
  }

  /** @return */
  @RequestMapping(
      value = {"/", "/index"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object index() {
    return new HttpDataResponse(HttpCode.PERMISSION_DENIED);
  }

  /**
   * 获取uuid
   *
   * @param session
   * @return
   */
  @GetMapping(
      value = {"/getUUID"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  @CheckUserInfo
  public Object getUUID(HttpSession session) {
    try {
      String userInfoStr = (String) session.getAttribute("userInfo");
      JSONObject userInfo = JSON.parseObject(userInfoStr);
      if (!userInfo.containsKey("uuid")) {
        return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
      }
      return new HttpDataResponse(HttpCode.SUCCESS, userInfo.getString("uuid"));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    }
  }

  /**
   * 检查登陆状态
   *
   * @return
   */
  @GetMapping(
      value = {"/check"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object check() {
    JSONObject result = new JSONObject();
    result.put("status", true);
    return new HttpDataResponse(HttpCode.SUCCESS, result);
  }

  /**
   * 登陆
   *
   * @param session
   * @param loginRequest
   * @return
   */
  @PostMapping(
      value = {"/login"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object login(HttpSession session, @NotNull @RequestBody LoginRequest loginRequest) {
    if (checkUserUploadData(loginRequest)) {
      return new HttpDataResponse(HttpCode.DATA_DOES_NOT_EXIST);
    }
    if (!checkCaptchaCode(session, loginRequest.getCaptcha())) {
      return new HttpDataResponse(HttpCode.CAPTCHA_ERROR);
    }

    String username = loginRequest.getUsername().trim();
    String password = loginRequest.getPassword().trim();
    User userInfo = null;
    try {
      userInfo = userService.getUserInfo(username, password);
    } catch (UserNotFoundException e) {
      return new HttpDataResponse(HttpCode.USERNAME_OR_PASSWORD_ERROR);
    } catch (PasswordIncorrectException e) {
      return new HttpDataResponse(HttpCode.USERNAME_OR_PASSWORD_ERROR);
    } catch (SqlRuntimeException e) {
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    }

    session.setAttribute("uid", userInfo.getId());
    session.setAttribute("userInfo", JSON.toJSONString(userInfo));

    return new HttpDataResponse(HttpCode.SUCCESS);
  }

  /**
   * 修改密码
   *
   * @param session
   * @param req
   * @return
   */
  @PostMapping(
      value = {"/changePassword"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object changePassword(
      HttpSession session, @NotNull @RequestBody ChangePassowrdRequest req) {
    if (StringUtils.isEmpty(req.getCaptcha())
        || StringUtils.isEmpty(req.getOldPassword())
        || StringUtils.isEmpty(req.getNewPassword())
        || req.getOldPassword().trim().length() < 8
        || req.getNewPassword().trim().length() < 8) {
      return new HttpDataResponse(HttpCode.PASSWORD_FORMAT_ERROR);
    }
    if (!checkCaptchaCode(session, req.getCaptcha().trim())) {
      return new HttpDataResponse(HttpCode.CAPTCHA_ERROR);
    }
    String oldPassword = req.getOldPassword().trim();
    String newPassword = req.getNewPassword().trim();
    User userInfo = JSON.parseObject((String) session.getAttribute("userInfo"), User.class);
    try {
      boolean b = userService.changePassword(userInfo.getUsername(), oldPassword, newPassword);
      return b
          ? (new HttpDataResponse(HttpCode.SUCCESS))
          : (new HttpDataResponse(HttpCode.UNKNOWN_ERROR));
    } catch (SqlRuntimeException e) {
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    } catch (UserNotFoundException e) {
      return new HttpDataResponse(HttpCode.NO_SUCH_USER);
    } catch (PasswordIncorrectException e) {
      return new HttpDataResponse(HttpCode.USERNAME_OR_PASSWORD_ERROR);
    }
  }

  /**
   * 绑定邮箱
   *
   * @param session
   * @param req
   * @return
   */
  @PostMapping(
      value = {"/bindEmail"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object bindEmail(HttpSession session, @NotNull @RequestBody BindEmailRequest req) {
    if (StringUtils.isEmpty(req.getCaptcha())
        || StringUtils.isEmpty(req.getEmail())
        || !EmailValidator.getInstance().isValid(req.getEmail())) {
      return new HttpDataResponse(HttpCode.DATA_ERROR);
    }
    if (!checkCaptchaCode(session, req.getCaptcha().trim())) {
      return new HttpDataResponse(HttpCode.CAPTCHA_ERROR);
    }
    User userInfo = JSON.parseObject((String) session.getAttribute("userInfo"), User.class);
    try {
      boolean b = userService.bindEmail(userInfo.getUsername(), req.getEmail());
      return b
          ? (new HttpDataResponse(HttpCode.SUCCESS))
          : (new HttpDataResponse(HttpCode.UNKNOWN_ERROR));
    } catch (SqlRuntimeException e) {
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    } catch (UserNotFoundException e) {
      return new HttpDataResponse(HttpCode.NO_SUCH_USER);
    } catch (EmailCheckedException e) {
      return new HttpDataResponse(HttpCode.EMAIL_FORMAT_ERROR);
    } catch (TooQuickOperationException e) {
      return new HttpDataResponse(HttpCode.OPERATION_TOO_FREQUENT);
    } catch (TooManyOperationException e) {
      return new HttpDataResponse(HttpCode.OPERATION_LIMIT);
    }
  }

  /**
   * 注册
   *
   * @param session
   * @param loginRequest
   * @return
   */
  @PostMapping(
      value = {"/reg"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object reg(HttpSession session, @Nullable @RequestBody LoginRequest loginRequest) {
    if (!regFlag) {
      return new HttpDataResponse(HttpCode.SUSPENDED_REGISTRATION);
    }
    if (checkUserUploadData(loginRequest)) {
      return new HttpDataResponse(HttpCode.DATA_DOES_NOT_EXIST);
    }
    if (!checkCaptchaCode(session, loginRequest.getCaptcha())) {
      return new HttpDataResponse(HttpCode.CAPTCHA_ERROR);
    }
    String username = loginRequest.getUsername().trim();
    String password = loginRequest.getPassword().trim();
    if (username.length() < 5) {
      return new HttpDataResponse(HttpCode.USERNAME_FORMAT_ERROR);
    }
    if (password.length() < 8) {
      return new HttpDataResponse(HttpCode.PASSWORD_FORMAT_ERROR);
    }
    boolean insertUserResult = false;
    try {
      insertUserResult = userService.insertUser(username, password);
    } catch (SqlRuntimeException e) {
      new HttpDataResponse(HttpCode.DATABASE_ERROR);
    } catch (UserAlreadyExistException e) {
      return new HttpDataResponse(HttpCode.USER_EXISTS);
    }
    return insertUserResult
        ? (new HttpDataResponse(HttpCode.SUCCESS))
        : (new HttpDataResponse(HttpCode.USER_EXISTS));
  }

  /**
   * 登出
   *
   * @param session
   * @return
   */
  @RequestMapping(
      value = {"/logout"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object logout(HttpSession session) {

    session.invalidate();
    return new HttpDataResponse(HttpCode.SUCCESS);
  }

  /**
   * 获取用户部分验证码
   *
   * @param session
   * @param rsp
   */
  @RequestMapping(
      value = {"/captcha"},
      produces = "image/png")
  public void captcha(HttpSession session, HttpServletResponse rsp) {
    rsp.setDateHeader("Expires", 0);
    rsp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    rsp.addHeader("Cache-Control", "post-check=0, pre-check=0");
    rsp.setHeader("Pragma", "no-cache");
    String capText = captchaProducer.createText();

    session.setAttribute(USER_CAPTCHA_CODE_KEY, capText);
    session.setAttribute(USER_CAPTCHA_GEN_TIME_KEY, System.currentTimeMillis() + "");

    BufferedImage bi = captchaProducer.createImage(capText);

    try (ServletOutputStream out = rsp.getOutputStream()) {
      ImageIO.write(bi, "png", out);
    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
      rsp.setStatus(502);
      return;
    }
  }
}
