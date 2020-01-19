package com.agh0st.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SqlRuntimeException extends Exception {
  public SqlRuntimeException(String s) {
    super(s);
  }
}
