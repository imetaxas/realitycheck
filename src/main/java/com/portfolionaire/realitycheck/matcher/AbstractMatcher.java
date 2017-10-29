package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.matchervalidator.MatcherValidator;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class AbstractMatcher<T, K> implements Matchable<T> {

  public T actual;
  public K actualValue;
  private MatcherValidator<T, K> validator;
  public Reader reader;

  public AbstractMatcher() {
  }

  public AbstractMatcher(T actual, MatcherValidator<T, K> validator, Reader reader) {
    this.actual = actual;
    this.validator = validator;
    this.reader = reader;
  }

  @Override
  public Matchable<T> match() throws ValidationException {
    validator.preValidate(actual);
    actualValue =  (K) reader.read();
    validator.postValidate(actualValue);
    return this;
  }
}
