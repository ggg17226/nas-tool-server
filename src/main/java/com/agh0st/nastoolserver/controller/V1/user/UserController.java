package com.agh0st.nastoolserver.controller.V1.user;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/V1/user")
@Log4j2
public class UserController {

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
}
