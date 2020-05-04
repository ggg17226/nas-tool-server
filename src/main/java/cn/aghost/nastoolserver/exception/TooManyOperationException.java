package cn.aghost.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TooManyOperationException extends Exception {
  public TooManyOperationException(String s) {
    super(s);
  }
}
