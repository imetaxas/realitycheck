package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;
import com.portfolionaire.realitycheck.validator.Validator;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class AbstractMatcher<T, K> implements Matchable<T> {

  public K actual;
  private Validator<T, K> validator;
  public Reader reader;

  public AbstractMatcher(Validator<T, K> validator, Reader reader) {
    this.validator = validator;
    this.reader = reader;
  }

  @Override
  public Matchable<T> validate() throws ValidationException {
    actual = (K) validator.validatedValue(reader);
    return this;
  }
}
