package com.portfolionaire.realitycheck.exception;

/**
 * @author yanimetaxas
 */
public class ValidationException extends Exception {

  public ValidationException(Throwable cause) {
    super(cause);
  }

  public ValidationException(String message) {
    super(message);
  }
}
