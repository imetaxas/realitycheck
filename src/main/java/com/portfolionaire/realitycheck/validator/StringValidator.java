package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 08-Oct-17.
 */
public class StringValidator<T> implements Validator<String> {

  @Override
  public boolean validate(String string) throws ValidationException {
    throw new ValidationException("");
  }
}
