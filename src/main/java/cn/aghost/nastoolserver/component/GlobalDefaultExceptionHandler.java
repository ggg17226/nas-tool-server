package cn.aghost.nastoolserver.component;

import cn.aghost.nastoolserver.exception.UserInfoNotExistException;
import cn.aghost.nastoolserver.object.response.HttpCode;
import cn.aghost.nastoolserver.object.response.HttpDataResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Object defaultExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
    if (e instanceof UserInfoNotExistException) {
      return new HttpDataResponse(HttpCode.SUSPENDED_REGISTRATION);
    } else throw e;
  }
}
