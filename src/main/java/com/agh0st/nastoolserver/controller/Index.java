package com.agh0st.nastoolserver.controller;

import com.agh0st.nastoolserver.object.response.HttpCode;
import com.agh0st.nastoolserver.object.response.HttpDataResponse;
import com.agh0st.nastoolserver.object.response.ServerTimeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.TreeMap;

@RestController
@RequestMapping("/")
@Slf4j
public class Index {
  @RequestMapping(
      value = {"/", "/index"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object index() {
    return new HttpDataResponse(HttpCode.PERMISSION_DENIED);
  }

  @GetMapping(
      value = {"/serverTime"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object serverTime() {
    HttpDataResponse httpDataResponse = new HttpDataResponse(HttpCode.SUCCESS);
    httpDataResponse.setData(new ServerTimeResponse((int) (System.currentTimeMillis() / 1000)));
    return httpDataResponse;
  }

  /**
   * 获取所有返回值列表
   *
   * @return
   */
  @GetMapping(
      value = {"/codeInfo"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object codeInfo() {
    Field[] fields = HttpCode.class.getFields();
    TreeMap<Integer, Object> resultMap = new TreeMap<>();
    for (Field field : fields) {
      if (field
          .getAnnotatedType()
          .getType()
          .getTypeName()
          .equals(HttpCode.CodeObject.class.getTypeName())) {
        TreeMap<String, Object> tmpMap = new TreeMap<>();
        try {
          HttpCode.CodeObject object = (HttpCode.CodeObject) field.get(HttpCode.CodeObject.class);
          tmpMap.put("desc", object.getDesc());
          tmpMap.put("desc_CN", object.getDesc_cn());
          resultMap.put(object.getCode(), tmpMap);
        } catch (Exception e) {
          log.error("构造所有返回值结构出错：" + ExceptionUtils.getStackTrace(e));
          return new HttpDataResponse(HttpCode.SYSTEM_ERROR);
        }
      }
    }
    HttpDataResponse httpDataResponse = new HttpDataResponse(HttpCode.SUCCESS);
    httpDataResponse.setData(resultMap);
    return httpDataResponse;
  }
}
