package com.agh0st.nastoolserver.init;

import com.agh0st.nastoolserver.session.SessionInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@Log4j2
public class SessionInit implements WebMvcConfigurer {

  private static String[] excludePaths = {
    /** base api */
    "/",
    "/index",
    "/serverTime",
    "/codeInfo",
    /** user_api */
    "/V1/User/login",
    "/V1/User/logout"
  };

//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    InterceptorRegistration interceptorRegistration =
//        registry.addInterceptor(new SessionInterceptor());
//    Arrays.asList(excludePaths).stream()
//        .forEach(
//            item -> {
//              log.trace("add excludePath: {}", item);
//              interceptorRegistration.excludePathPatterns(item);
//            });
//    interceptorRegistration.addPathPatterns("/**");
//  }
}
