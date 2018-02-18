package com.yanimetaxas.realitycheck.asserter;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class IntegerAssert extends AbstractAssert<IntegerAssert, Integer> {

  public IntegerAssert(Integer integer) {
    super(integer, null);
  }

  public IntegerAssert(Integer integer, String customMessage) {
    super(integer, customMessage);
  }
}


