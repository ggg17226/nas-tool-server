package com.agh0st.nastoolserver.object.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
  private String captcha;
}
