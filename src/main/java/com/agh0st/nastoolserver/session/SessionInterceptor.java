package com.agh0st.nastoolserver.session;

import com.agh0st.nastoolserver.exception.SqlRuntimeException;
import com.agh0st.nastoolserver.exception.UserNotFoundException;
import com.agh0st.nastoolserver.object.entity.User;
import com.agh0st.nastoolserver.object.response.HttpCode;
import com.agh0st.nastoolserver.object.response.HttpDataResponse;
import com.agh0st.nastoolserver.service.UserService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
  @Resource private UserService userService;

  private void sendNeedLogin(HttpServletResponse rsp) throws IOException {
    rsp.setStatus(403);
    rsp.setHeader("Content-Type", "application/json;charset=utf-8");
    rsp.getWriter().write(JSON.toJSONString(new HttpDataResponse(HttpCode.NEED_LOGIN)));
  }

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse rsp, Object handler)
      throws IOException {
    HttpSession session = req.getSession();
    String userInfo = null;
    try {
      userInfo = (String) session.getAttribute("userInfo");
    } catch (Exception ex) {
      userInfo = null;
    }
    long lastCheck = 0;
    try {
      lastCheck = (long) session.getAttribute("lastCheck");
    } catch (Exception ex) {
      lastCheck = 0;
    }
    if (!StringUtils.isEmpty(userInfo)) {
      if ((System.currentTimeMillis() - lastCheck) > 300000) {
        User user = JSON.parseObject(userInfo, User.class);
        if (StringUtils.isEmpty(user.getUsername())) {
          session.invalidate();
          sendNeedLogin(rsp);
          return false;
        }
        User user1 = null;
        try {
          user1 = userService.getUserInfo(user.getUsername());
        } catch (UserNotFoundException | SqlRuntimeException e) {
          session.invalidate();
          sendNeedLogin(rsp);
          return false;
        }
        if (user1.getPasswd().equals(user.getPasswd())) {
          String UserInfo1 = JSON.toJSONString(user1);
          if (!UserInfo1.equals(userInfo)) {
            session.setAttribute("userInfo", UserInfo1);
          }
          session.setAttribute("lastCheck", System.currentTimeMillis());
          return true;
        } else {
          session.invalidate();
          sendNeedLogin(rsp);
          return false;
        }
      } else {
        return true;
      }
    } else {
      sendNeedLogin(rsp);
      return false;
    }
  }
}
