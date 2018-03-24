package com.yanimetaxas.realitycheck;

/**
 * @author yanimetaxas
 */
interface Checkable<SELF extends Checkable<SELF, ACTUAL>, ACTUAL> {

  SELF isNull();

  SELF isNotNull();

  SELF isEqualTo(ACTUAL expected);

  SELF isNotEqualTo(ACTUAL expected);

}
