package com.agh0st.nastoolserver.controller.V1.user;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.PO.User;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import com.agh0st.nastoolserver.service.UserService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@RestController
@RequestMapping("/V1/user")
@Log4j2
public class UserController {
  @Resource private UserService userService;
  @Resource private Producer captchaProducer;

  private static final String USER_CAPTCHA_CODE_KEY = "user-captcha-code";
  private static final String USER_CAPTCHA_GEN_TIME_KEY = "user-captcha-gen-time";

  /**
   * 检查提交数据
   *
   * @param data
   * @return
   */
  private boolean checkUserUploadData(Map<String, String> data) {
    return data == null
        || !data.containsKey("username")
        || !data.containsKey("password")
        || StringUtils.isEmpty(data.get("password"))
        || StringUtils.isEmpty(data.get("username"))
        || StringUtils.isEmpty(data.get("captcha"));
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
    }
    try {
      session.removeAttribute(USER_CAPTCHA_CODE_KEY);
    } catch (Exception e) {
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
    try {
      code = session.getAttribute(USER_CAPTCHA_CODE_KEY).toString();
      time = session.getAttribute(USER_CAPTCHA_GEN_TIME_KEY).toString();
    } catch (Exception e) {
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

  /** @return */
  @RequestMapping(
      value = {"/", "/index"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object index() {
    return new HttpDataVo(HttpCode.PERMISSION_DENIED);
  }

  /**
   * 获取uuid
   *
   * @param session
   * @return
   */
  @RequestMapping(
      value = {"/getUUID"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object getUUID(HttpSession session) {
    try {
      String userInfoStr = (String) session.getAttribute("userInfo");
      if (userInfoStr == null || StringUtils.isEmpty(userInfoStr)) {
        return new HttpDataVo(HttpCode.SYSTEM_ERROR);
      }
      JSONObject userInfo = JSON.parseObject(userInfoStr);
      if (userInfo == null || userInfo.size() < 1 || !userInfo.containsKey("uuid")) {
        return new HttpDataVo(HttpCode.SYSTEM_ERROR);
      }
      return new HttpDataVo(HttpCode.SUCCESS, userInfo.getString("uuid"));
    } catch (Exception e) {
      return new HttpDataVo(HttpCode.SYSTEM_ERROR);
    }
  }

  /**
   * 检查登陆状态
   *
   * @return
   */
  @RequestMapping(
      value = {"/check"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object check(HttpSession session) {
    JSONObject result = new JSONObject();
    result.put("status", true);
    return new HttpDataVo(HttpCode.SUCCESS, result);
  }

  /**
   * 登陆
   *
   * @param session
   * @param req
   * @param data
   * @return
   */
  @RequestMapping(
      value = {"/login"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object login(
      HttpSession session,
      HttpServletRequest req,
      @Nullable @RequestBody TreeMap<String, String> data) {
    if (!req.getMethod().equals("POST")) {
      return new HttpDataVo(HttpCode.METHOD_DENIED);
    }
    if (checkUserUploadData(data)) {
      return new HttpDataVo(HttpCode.DATA_DOES_NOT_EXIST);
    }
    if (!checkCaptchaCode(session, data.get("captcha"))) {
      return new HttpDataVo(HttpCode.CAPTCHA_ERROR);
    }

    String username = data.get("username").trim();
    String password = data.get("password").trim();
    User userInfo = userService.getUserInfo(username, password);

    if (userInfo == null) {
      return new HttpDataVo(HttpCode.USERNAME_OR_PASSWORD_ERROR);
    }

    session.setAttribute("uid", userInfo.getId());
    session.setAttribute("userInfo", JSON.toJSONString(userInfo));

    return new HttpDataVo(HttpCode.SUCCESS);
  }

  /**
   * 注册
   *
   * @param session
   * @param req
   * @param data
   * @return
   */
  @RequestMapping(
      value = {"/reg"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object reg(
      HttpSession session,
      HttpServletRequest req,
      @Nullable @RequestBody TreeMap<String, String> data) {
    if (!req.getMethod().equals("POST")) {
      return new HttpDataVo(HttpCode.METHOD_DENIED);
    }
    if (checkUserUploadData(data)) {
      return new HttpDataVo(HttpCode.DATA_DOES_NOT_EXIST);
    }
    if (!checkCaptchaCode(session, data.get("captcha"))) {
      return new HttpDataVo(HttpCode.CAPTCHA_ERROR);
    }
    String username = data.get("username").trim();
    String password = data.get("password").trim();
    if (username.length() < 5) {
      return new HttpDataVo(HttpCode.USERNAME_FORMAT_ERROR);
    }
    if (password.length() < 8) {
      return new HttpDataVo(HttpCode.PASSWORD_FORMAT_ERROR);
    }
    boolean insertUserResult =
        userService.insertUser(data.get("username").trim(), data.get("password").trim());
    return insertUserResult
        ? (new HttpDataVo(HttpCode.SUCCESS))
        : (new HttpDataVo(HttpCode.USER_EXISTS));
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
    return new HttpDataVo(HttpCode.SUCCESS);
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
    ServletOutputStream out = null;
    try {
      out = rsp.getOutputStream();
    } catch (Exception e) {
      rsp.setStatus(502);
      return;
    }

    try {
      ImageIO.write(bi, "png", out);
    } catch (Exception e) {
      rsp.setStatus(502);
    } finally {
      try {
        out.close();
      } catch (Exception e) {
      }
    }
  }
}
