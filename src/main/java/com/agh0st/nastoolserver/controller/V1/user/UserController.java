package com.agh0st.nastoolserver.controller.V1.user;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import com.agh0st.nastoolserver.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.TreeMap;
import java.util.UUID;

@RestController
@RequestMapping("/V1/user")
@Log4j2
public class UserController {
  @Resource private UserService userService;

  @RequestMapping(
      value = {"/", "/index"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object index() {
    return new HttpDataVo(HttpCode.PERMISSION_DENIED);
  }

  @RequestMapping(
      value = {"/check"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object check() {
    return new HttpDataVo(HttpCode.PERMISSION_DENIED);
  }

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

    return new HttpDataVo(HttpCode.SUCCESS, userService.generateSalt());
  }

  @RequestMapping(
      value = {"/logout"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object logout(HttpSession session) {
    session.invalidate();
    return new HttpDataVo(HttpCode.SUCCESS);
  }
}
