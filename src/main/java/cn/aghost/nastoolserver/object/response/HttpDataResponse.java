package cn.aghost.nastoolserver.object.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class HttpDataResponse implements Serializable {
  int code;
  String desc;
  Object data;

  public HttpDataResponse(HttpCode.CodeObject codeObj) {
    this.code = codeObj.getCode();
    this.desc = codeObj.getDesc_cn();
  }

  public HttpDataResponse(HttpCode.CodeObject codeObj, Object data) {
    this.code = codeObj.getCode();
    this.desc = codeObj.getDesc_cn();
    this.data = data;
  }

  public HttpDataResponse(HttpCode.CodeObject codeObj, Object data, boolean useCN) {
    this.code = codeObj.getCode();
    this.desc = useCN ? codeObj.getDesc_cn() : codeObj.getDesc();
    this.data = data;
  }

  public HttpDataResponse(HttpCode.CodeObject codeObj, boolean useCN) {
    this.code = codeObj.getCode();
    this.desc = useCN ? codeObj.getDesc_cn() : codeObj.getDesc();
  }

  public HttpDataResponse() {}
}
