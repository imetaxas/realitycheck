package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.validator.Validator;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class AbstractMatcher<T, K> implements Matchable<T> {

  public T actual;
  private Validator<T> validator;

  public AbstractMatcher(T actual, Validator<T> validator) {
    this.actual = actual;
    this.validator = validator;
  }

  @Override
  public Matchable<T> validate() throws ValidationException {
    validator.validate(actual);
    return this;
  }
}
