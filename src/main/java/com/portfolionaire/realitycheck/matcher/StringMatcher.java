package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.validator.StringValidator;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class StringMatcher<T, K> extends AbstractMatcher<String, K> implements Matchable<String> {

  public StringMatcher(String str, StringValidator<T> stringValidator) {
    super(str, stringValidator);
  }
}
