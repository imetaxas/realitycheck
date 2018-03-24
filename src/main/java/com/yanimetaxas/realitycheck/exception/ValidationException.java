package com.yanimetaxas.realitycheck.exception;

/**
 * @author yanimetaxas
 */
public final class ValidationException extends AssertionError {

  public ValidationException(String message) {
    super(message);
  }
}
