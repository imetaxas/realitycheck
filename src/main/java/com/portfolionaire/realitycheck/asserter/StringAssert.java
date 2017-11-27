package com.portfolionaire.realitycheck.asserter;

/**
 * @author yanimetaxas
 */
public class StringAssert extends AbstractAssert<StringAssert, String> {

  public StringAssert(String s) {
    super(s);
  }

  public StringAssert hasLength(int size) throws AssertionError {
    if(actual.length() != size) {
      throw new AssertionError("Wrong length");
    }
    return self;
  }
}
