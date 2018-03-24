package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.util.GenericClass;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
public abstract class AbstractCheck<SELF extends AbstractCheck<SELF, ACTUAL>, ACTUAL> implements
    Checkable<SELF, ACTUAL> {

  final ACTUAL actual;
  final SELF self;
  final String customMessage;

  AbstractCheck(ACTUAL actual, String customMessage) {
    GenericClass<SELF> genericClass = new GenericClass(getClass());
    self = genericClass.getType().cast(this);
    this.actual = actual;
    this.customMessage = customMessage;
  }

  @Override
  public SELF isNull() {
    if (this.actual != null) {
      throwAssertionErrorWithCustomMessage("Subject is NULL");
    }
    return self;
  }

  @Override
  public SELF isNotNull() {
    if (this.actual == null) {
      throwAssertionErrorWithCustomMessage("Subject is not NULL");
    }
    return self;
  }

  @Override
  public SELF isEqualTo(ACTUAL expected) {
    if (!this.actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Subject is not equal");
    }
    return self;
  }

  @Override
  public SELF isNotEqualTo(ACTUAL expected) {
    if (this.actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Subject is equal");
    }
    return self;
  }

  void throwAssertionErrorWithCustomMessage(String defaultMessage) {
    if (customMessage == null) {
      throw new AssertionError(defaultMessage);
    } else {
      throw new AssertionError(customMessage);
    }
  }

  private ACTUAL getActualOrThrow(AssertionError e) throws AssertionError {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  ACTUAL getActual() throws AssertionError {
    return getActualOrThrow(new AssertionError("Cannot get ACTUAL"));
  }
}
