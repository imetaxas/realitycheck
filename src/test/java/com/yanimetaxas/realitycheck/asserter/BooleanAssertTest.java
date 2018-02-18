package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class BooleanAssertTest {

  @Test
  public void isTrue() throws Exception {
    assertNotNull(checkThat(true).isTrue());
  }

  @Test
  public void isFalse() throws Exception {
    assertNotNull(checkThat(false).isFalse());
  }

  @Test(expected = AssertionError.class)
  public void checkFalseTrue_WhenIsNot() throws Exception {
    checkThat(true).isFalse();
  }

  @Test(expected = AssertionError.class)
  public void isTrue_WhenIsNot() throws Exception {
    checkThat(false).isTrue();
  }
}