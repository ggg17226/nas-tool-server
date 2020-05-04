package cn.aghost.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SqlRuntimeException extends Exception {
  public SqlRuntimeException(String s) {
    super(s);
  }
}
