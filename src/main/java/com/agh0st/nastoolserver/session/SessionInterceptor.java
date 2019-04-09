package com.agh0st.nastoolserver.session;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.client.HttpResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
public class SessionInterceptor implements HandlerInterceptor {
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
    if (!StringUtils.isEmpty(userInfo)) {
      return true;
    } else {
      rsp.setStatus(200);
      rsp.setHeader("Content-Type", "application/json;charset=utf-8");
      rsp.getWriter().write(JSON.toJSONString(new HttpDataVo(HttpCode.NEED_LOGIN)));
      return false;
    }
  }
}
