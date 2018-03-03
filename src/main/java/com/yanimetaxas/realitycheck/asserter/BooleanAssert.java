package com.yanimetaxas.realitycheck.asserter;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class BooleanAssert extends AbstractAssert<BooleanAssert, Boolean> {

  public BooleanAssert(Boolean aBoolean) {
    super(aBoolean, null);
  }

  public BooleanAssert(Boolean aBoolean, String customMessage) {
    super(aBoolean, customMessage);
  }

  public BooleanAssert isTrue() throws AssertionError {
    if(!actual) {
      throwAssertionErrorWithCustomMessage("Is false");
    }
    return self;
  }

  public BooleanAssert isFalse() throws AssertionError {
    if(actual) {
      throwAssertionErrorWithCustomMessage("Is true");
    }
    return self;
  }
}
