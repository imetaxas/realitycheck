package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 09-Nov-17.
 */
public class StringAssert extends AbstractAssert<StringAssert, String> {

  public StringAssert(String s) {
    super(s);
  }

  public StringAssert hasLength(int size) throws ValidationException {
    if(actual.length() != size) {
      throw new ValidationException("");
    }
    return self;
  }
}
