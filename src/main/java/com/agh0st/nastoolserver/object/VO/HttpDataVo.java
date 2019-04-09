package com.agh0st.nastoolserver.object.VO;

import com.agh0st.nastoolserver.component.HttpCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class HttpDataVo implements Serializable {
  int code;
  String desc;
  Object data;

  public HttpDataVo(HttpCode.CodeObject codeObj) {
    this.code = codeObj.getCode();
    this.desc = codeObj.getDesc_cn();
  }

  public HttpDataVo(HttpCode.CodeObject codeObj, Object data) {
    this.code = codeObj.getCode();
    this.desc = codeObj.getDesc_cn();
    this.data = data;
  }

  public HttpDataVo(HttpCode.CodeObject codeObj, Object data, boolean useCN) {
    this.code = codeObj.getCode();
    this.desc = useCN ? codeObj.getDesc_cn() : codeObj.getDesc();
    this.data = data;
  }

  public HttpDataVo(HttpCode.CodeObject codeObj, boolean useCN) {
    this.code = codeObj.getCode();
    this.desc = useCN ? codeObj.getDesc_cn() : codeObj.getDesc();
  }

  public HttpDataVo() {}
}
