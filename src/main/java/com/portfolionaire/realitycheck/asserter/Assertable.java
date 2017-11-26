package com.portfolionaire.realitycheck.asserter;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Assertable<SELF extends Assertable<SELF, ACTUAL>, ACTUAL> {

  SELF isNull();

  SELF isNotNull();

}
