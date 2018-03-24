package com.yanimetaxas.realitycheck;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public final class IntegerCheck extends AbstractCheck<IntegerCheck, Integer> {

  IntegerCheck(Integer integer) {
    super(integer, null);
  }

  IntegerCheck(Integer integer, String customMessage) {
    super(integer, customMessage);
  }
}


