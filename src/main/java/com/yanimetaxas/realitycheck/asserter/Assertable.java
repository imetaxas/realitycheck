package com.yanimetaxas.realitycheck.asserter;

/**
 * @author yanimetaxas
 */
public interface Assertable<SELF extends Assertable<SELF, ACTUAL>, ACTUAL> {

  SELF isNull();

  SELF isNotNull();

}
