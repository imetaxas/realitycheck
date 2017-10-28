package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
class AbstractValidator<T, K> implements Validator<T, K> {

  T value;

  @Deprecated
  public AbstractValidator() {}


  public AbstractValidator(T value) {
    this.value = value;
  }

  @Override
  public K validate(T value) throws ValidationException {
    if (value == null) {
      throw new ValidationException("Value is NULL");
    }
    return (K) value;
  }

  @Override
  public K doAction() throws ValidationException {
    return validate(value);
  }
}
