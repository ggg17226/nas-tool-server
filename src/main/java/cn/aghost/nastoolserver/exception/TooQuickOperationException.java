package cn.aghost.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TooQuickOperationException extends Exception {
  public TooQuickOperationException(String s) {
    super(s);
  }
}
