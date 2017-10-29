package com.portfolionaire.realitycheck.asserter;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Assertable<SELF extends Assertable<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, ACTUAL_VALUE> {

  SELF isNull();
  SELF isNotNull();
  //SELF isSameAs(Object expected) throws AssertionError;
  //SELF isNotSameAs(Object expected) throws AssertionError;

}
