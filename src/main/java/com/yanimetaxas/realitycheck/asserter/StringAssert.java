package com.yanimetaxas.realitycheck.asserter;

/**
 * @author yanimetaxas
 */
public class StringAssert extends AbstractAssert<StringAssert, String> {

  public StringAssert(String string) {
    super(string, null);
  }

  public StringAssert(String string, String message) {
    super(string, message);
  }

  public StringAssert hasLength(int size) throws AssertionError {
    if(actual.length() != size) {
      throwProperAssertionError("Wrong length");
    }
    return self;
  }
}
