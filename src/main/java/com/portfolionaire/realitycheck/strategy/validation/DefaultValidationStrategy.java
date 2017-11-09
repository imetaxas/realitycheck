package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 29-Oct-17.
 */
public class DefaultValidationStrategy<T, K> extends AbstractValidationStrategy<T, T> {

  public DefaultValidationStrategy(T actual) {
    super(actual);
  }

  @Override
  public T validate() throws ValidationException {
    return actual.orElseThrow(() -> new ValidationException("No value present"));
  }
}
