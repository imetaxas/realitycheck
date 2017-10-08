package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.exception.ValidationException;
import com.portfolionaire.truecsv.validator.Validator;

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
