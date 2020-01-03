package com.agh0st.nastoolserver.controller.V1.utils;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.PO.User;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import com.agh0st.nastoolserver.service.UserService;
import com.agh0st.nastoolserver.utils.AliPush;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/V1/test")
@Log4j2
public class TestController {
  @Resource private UserService userService;
  @Resource private AliPush aliPush;

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
      return new HttpDataVo(HttpCode.DATA_ERROR);
    }
    User userInfo = userService.getUserInfo(username);
    if (userInfo == null) {
      return new HttpDataVo(HttpCode.NO_SUCH_USER);
    }
    return new HttpDataVo(
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
      return new HttpDataVo(HttpCode.DATA_ERROR);
    }
    User userInfo = userService.getUserInfo(username);
    if (userInfo == null) {
      return new HttpDataVo(HttpCode.NO_SUCH_USER);
    }
    return new HttpDataVo(
        HttpCode.SUCCESS,
        aliPush.pushNoticeToIos(userInfo.getUuid(), title, body, env, payload) ? "T" : "F");
  }
}
