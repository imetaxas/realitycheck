package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.reader.StringReader;
import com.portfolionaire.realitycheck.validator.StringValidator;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class StringMatcher extends AbstractMatcher<String, String> implements Matchable<String> {

  public StringMatcher(String str, StringValidator stringValidator) {
    super(stringValidator, new StringReader(str));
  }
}
