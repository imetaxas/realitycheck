package com.yanimetaxas.realitycheck.exception;

/**
 * @author yanimetaxas
 */
public class ValidationException extends AssertionError {

  public ValidationException(String message) {
    super(message);
  }
}
