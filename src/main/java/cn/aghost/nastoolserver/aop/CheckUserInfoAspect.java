package cn.aghost.nastoolserver.aop;

import cn.aghost.nastoolserver.exception.UserInfoNotExistException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
@Slf4j
public class CheckUserInfoAspect {
  @Pointcut("@annotation(cn.aghost.nastoolserver.aop.interfaces.CheckUserInfo)")
  public void annotationPoint() {}

  @Before("annotationPoint()")
  public void validator() throws UserInfoNotExistException {
    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    HttpSession session = request.getSession();
    String userInfoStr = (String) session.getAttribute("userInfo");
    if (userInfoStr == null || StringUtils.isEmpty(userInfoStr)) {
      throw new UserInfoNotExistException();
    }
    JSONObject userInfo = JSON.parseObject(userInfoStr);
    if (userInfo == null || userInfo.size() < 1) {
      throw new UserInfoNotExistException();
    }
  }
}
