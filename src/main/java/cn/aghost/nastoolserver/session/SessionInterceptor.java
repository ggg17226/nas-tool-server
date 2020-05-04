package cn.aghost.nastoolserver.session;

import cn.aghost.nastoolserver.exception.SqlRuntimeException;
import cn.aghost.nastoolserver.exception.UserNotFoundException;
import cn.aghost.nastoolserver.object.entity.User;
import cn.aghost.nastoolserver.object.response.HttpCode;
import cn.aghost.nastoolserver.object.response.HttpDataResponse;
import cn.aghost.nastoolserver.service.UserService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cn.aghost.nastoolserver.object.staticData.RedisKeys.USER_NEED_UPDATE_CACHE_KEY_PREFIX;

@Configuration
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
  @Resource private UserService userService;
  @Resource private StringRedisTemplate stringRedisTemplate;

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
      User user = JSON.parseObject(userInfo, User.class);
      if ((System.currentTimeMillis() - lastCheck) > 300000
          || stringRedisTemplate.hasKey(USER_NEED_UPDATE_CACHE_KEY_PREFIX + user.getId())) {
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
          if (stringRedisTemplate.hasKey(USER_NEED_UPDATE_CACHE_KEY_PREFIX + user.getId())) {
            stringRedisTemplate.delete(USER_NEED_UPDATE_CACHE_KEY_PREFIX + user.getId());
          }
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
