package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
abstract class AbstractValidator<T, K> implements Validator<K> {

  private T actual;

  AbstractValidator(T actual) {
    this.actual = actual;
  }

  T getActualOrElseNull() {
    return Optional.ofNullable(actual).orElse(null);
  }

  T getActualOrThrow(ValidationException e) throws ValidationException {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  @Override
  public K validate() throws ValidationException {
    if (actual == Optional.empty()) {
      throw new ValidationException("ACTUAL is NULL");
    }
    return null;
  }
}
