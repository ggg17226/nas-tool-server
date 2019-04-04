package com.agh0st.nastoolserver.controller;

import com.agh0st.nastoolserver.component.HttpCode;
import com.agh0st.nastoolserver.object.VO.HttpDataVo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.TreeMap;

@RestController
@RequestMapping("/")
@Log4j2
public class Index {
  @RequestMapping(
      value = {"/", "/index"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object index() {
    return new HttpDataVo(HttpCode.PERMISSION_DENIED);
  }

  @RequestMapping(
      value = {"/serverTime"},
      produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object serverTime() {
    HttpDataVo httpDataVo = new HttpDataVo(HttpCode.SUCCESS);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("timestamp", (int) (System.currentTimeMillis() / 1000));
    httpDataVo.setData(jsonObject);
    return httpDataVo;
  }

  /**
   * 获取所有返回值列表
   *
   * @return
   */
  @RequestMapping(
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
          return new HttpDataVo(HttpCode.SYSTEM_ERROR);
        }
      }
    }
    HttpDataVo httpDataVo = new HttpDataVo(HttpCode.SUCCESS);
    httpDataVo.setData(resultMap);
    return httpDataVo;
  }
}
