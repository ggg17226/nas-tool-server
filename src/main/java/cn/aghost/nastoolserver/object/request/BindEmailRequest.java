package cn.aghost.nastoolserver.object.request;

import lombok.Data;

@Data
public class BindEmailRequest {
  private String captcha;
  private String email;
}
