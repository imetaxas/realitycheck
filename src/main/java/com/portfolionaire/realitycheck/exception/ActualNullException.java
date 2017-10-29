package com.portfolionaire.realitycheck.exception;

/**
 * Created by imeta on 28-Oct-17.
 */
public class ActualNullException extends ValidationException {

  public ActualNullException(Throwable cause) {
    super(cause);
  }

  public ActualNullException(String message) {
    super(message);
  }


}
