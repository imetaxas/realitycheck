package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.matcher.Matchable;

/**
 * @author yanimetaxas
 */
abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements Assertable<SELF, ACTUAL> {

  final ACTUAL actual;
  final SELF self;
  Matchable matcher;

  public AbstractAssert(ACTUAL actual, Class<?> selfType) {
    self = (SELF) selfType.cast(this);
    this.actual = actual;
  }

  @Override
  public SELF isNull() {
    if(this.actual != null) {
      throw new AssertionError();
    }
    return self;
  }

  /*public Matchable asserts(Matchable matchable) {
    try {
      return matchable.match();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }*/

  public boolean isMatcherNull() {
    return matcher == null;
  }
}
