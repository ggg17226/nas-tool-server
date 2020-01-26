package com.agh0st.nastoolserver.object.request;

import lombok.Data;

@Data
public class ChangePassowrdRequest {
  private String oldPassword;
  private String newPassword;
  private String captcha;
}
