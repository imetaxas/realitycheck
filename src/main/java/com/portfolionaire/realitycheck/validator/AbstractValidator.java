package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
class AbstractValidator<T, K> implements Validator<T, K> {

  Optional<T> value;

  public AbstractValidator(T value) {
    this.value = Optional.ofNullable(value);
  }

  @Override
  public K validate() throws ValidationException {
    if (value == Optional.empty()) {
      throw new ValidationException("ACTUAL is NULL");
    }
    return (K) value;
  }

  @Override
  public K doAction() throws ValidationException {
    return validate();
  }
}
