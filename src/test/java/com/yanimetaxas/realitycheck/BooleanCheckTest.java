package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class BooleanCheckTest {

  @Test(expected = AssertionError.class)
  public void checkFalseTrue_WhenIsNot() throws Exception {
    checkThat(true).isFalse();
  }

  @Test(expected = AssertionError.class)
  public void isTrue_WhenIsNot() throws Exception {
    checkThat(false).isTrue();
  }
}