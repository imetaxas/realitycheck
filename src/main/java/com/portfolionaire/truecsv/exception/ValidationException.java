package com.portfolionaire.truecsv.exception;

/**
 * Created by imeta on 07-Oct-17.
 */
public class ValidationException extends Exception {


  public ValidationException(Throwable cause) {
    super(cause);
  }

  public ValidationException(String message) {
    super(message);
  }
}
