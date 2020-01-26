package com.agh0st.nastoolserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TooManyOperationException extends Exception {
  public TooManyOperationException(String s) {
    super(s);
  }
}
