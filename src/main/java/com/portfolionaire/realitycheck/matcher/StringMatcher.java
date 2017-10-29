package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.matchervalidator.MatcherValidator;
import com.portfolionaire.realitycheck.reader.StringReader;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class StringMatcher<T, K> extends AbstractMatcher<byte[], K> implements Matchable<byte[]> {

  public StringMatcher(String str, MatcherValidator<byte[], K> stringValidator) {
    super(str.getBytes(), stringValidator, new StringReader(str));
  }
}
