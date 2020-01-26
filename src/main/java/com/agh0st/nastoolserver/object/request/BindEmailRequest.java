package com.agh0st.nastoolserver.object.request;

import lombok.Data;

@Data
public class BindEmailRequest {
  private String captcha;
  private String email;
}
