package com.portfolionaire.truecsv.validator;

import com.portfolionaire.truecsv.exception.ValidationException;

/**
 * Created by imeta on 08-Oct-17.
 */
public class StringValidator<T> implements Validator<String> {

  @Override
  public boolean validate(String string) throws ValidationException {
    throw new ValidationException("");
  }
}
