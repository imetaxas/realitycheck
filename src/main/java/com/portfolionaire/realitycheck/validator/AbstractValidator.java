package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
class AbstractValidator<T> implements Validator<T> {

  @Override
  public T validate(T value) throws ValidationException {
    if (value == null) {
      throw new ValidationException("Value is NULL");
    }
    return value;
  }
}
