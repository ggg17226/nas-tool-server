package cn.aghost.nastoolserver.controller.V1.utils;

import cn.aghost.nastoolserver.exception.SqlRuntimeException;
import cn.aghost.nastoolserver.exception.UserNotFoundException;
import cn.aghost.nastoolserver.object.entity.User;
import cn.aghost.nastoolserver.object.response.HttpCode;
import cn.aghost.nastoolserver.object.response.HttpDataResponse;
import cn.aghost.nastoolserver.service.UserService;
import cn.aghost.nastoolserver.utils.AliPush;
import cn.aghost.nastoolserver.utils.XiaoMiPush;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//@RestController
//@RequestMapping("/V1/test")
@Slf4j
public class TestController {
  @Resource private UserService userService;
  @Resource private AliPush aliPush;
  @Resource private XiaoMiPush xiaomiPush;

  @RequestMapping(
      value = {"/sendMsg"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object sendMsg(
      @RequestParam @Nullable String username,
      @RequestParam @Nullable String title,
      @RequestParam @Nullable String body,
      @RequestParam @Nullable String env,
      @RequestParam @Nullable String payload) {
    if (StringUtils.isEmpty(username)
        || StringUtils.isEmpty(title)
        || StringUtils.isEmpty(body)
        || StringUtils.isEmpty(env)
        || StringUtils.isEmpty(payload)) {
      return new HttpDataResponse(HttpCode.DATA_ERROR);
    }
    User userInfo = null;
    try {
      userInfo = userService.getUserInfo(username);
    } catch (SqlRuntimeException e) {
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    } catch (UserNotFoundException e) {
      return new HttpDataResponse(HttpCode.NO_SUCH_USER);
    }
    return new HttpDataResponse(
        HttpCode.SUCCESS,
        aliPush.pushMessageToIos(userInfo.getUuid(), title, body, env, payload) ? "T" : "F");
  }

  @RequestMapping(
      value = {"/sendNotice"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object sendNotice(
      @RequestParam @Nullable String username,
      @RequestParam @Nullable String title,
      @RequestParam @Nullable String body,
      @RequestParam @Nullable String env,
      @RequestParam @Nullable String payload) {
    if (StringUtils.isEmpty(username)
        || StringUtils.isEmpty(title)
        || StringUtils.isEmpty(body)
        || StringUtils.isEmpty(env)
        || StringUtils.isEmpty(payload)) {
      return new HttpDataResponse(HttpCode.DATA_ERROR);
    }
    User userInfo = null;
    try {
      userInfo = userService.getUserInfo(username);
    } catch (UserNotFoundException e) {
      return new HttpDataResponse(HttpCode.NO_SUCH_USER);
    } catch (SqlRuntimeException e) {
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    }
    return new HttpDataResponse(
        HttpCode.SUCCESS,
        aliPush.pushNoticeToIos(userInfo.getUuid(), title, body, env, payload) ? "T" : "F");
  }

  @GetMapping(
      value = {"/sendXiaomiMsg"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object sendXiaomiMsg() {
    try {
      String s = xiaomiPush.sendToAccount("aaaabbbb", "t_title", "{}", "t_desc");
      return new HttpDataResponse(HttpCode.SUCCESS, s);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
    }
  }
}
