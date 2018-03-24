package com.yanimetaxas.realitycheck;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public final class BooleanCheck extends AbstractCheck<BooleanCheck, Boolean> {

  BooleanCheck(Boolean aBoolean) {
    super(aBoolean, null);
  }

  BooleanCheck(Boolean aBoolean, String customMessage) {
    super(aBoolean, customMessage);
  }

  public BooleanCheck isTrue() throws AssertionError {
    if(!actual) {
      throwAssertionErrorWithCustomMessage("Is false");
    }
    return self;
  }

  public BooleanCheck isFalse() throws AssertionError {
    if(actual) {
      throwAssertionErrorWithCustomMessage("Is true");
    }
    return self;
  }
}
