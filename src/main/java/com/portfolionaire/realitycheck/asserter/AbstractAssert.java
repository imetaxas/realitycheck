package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.util.GenericClass;

/**
 * @author yanimetaxas
 */
public abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements
    Assertable<SELF, ACTUAL> {

  final ACTUAL actual;
  final SELF self;

  public AbstractAssert(ACTUAL actual) {
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

  /*@Override
  public SELF isSameAs(Object expected) throws AssertionError {
    if(!actual.equals(expected)) {
      throw new AssertionError();
    }
    return self;
  }

  @Override
  public SELF isNotSameAs(Object expected) throws AssertionError {
    if(actual.equals(expected)) {
      throw new AssertionError();
    }
    return self;
  }*/
}
