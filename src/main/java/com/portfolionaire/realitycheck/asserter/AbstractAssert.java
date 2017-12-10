package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.util.GenericClass;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements
    Assertable<SELF, ACTUAL> {

  final ACTUAL actual;
  final SELF self;

  AbstractAssert(ACTUAL actual) {
    GenericClass<SELF> genericClass = new GenericClass(getClass());
    self = genericClass.getType().cast(this);
    this.actual = actual;
  }

  @Override
  public SELF isNull() {
    if (this.actual != null) {
      throw new AssertionError();
    }
    return self;
  }

  @Override
  public SELF isNotNull() {
    if (this.actual == null) {
      throw new AssertionError();
    }
    return self;
  }

  public ACTUAL getActualOrThrow(AssertionError e) throws AssertionError {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  public ACTUAL getActual() throws AssertionError {
    return getActualOrThrow(new AssertionError("Cannot get ACTUAL"));
  }
}
