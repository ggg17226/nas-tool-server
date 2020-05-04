package cn.aghost.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class XiaomiPushException extends Exception {
  public XiaomiPushException(String s) {
    super(s);
  }
}
