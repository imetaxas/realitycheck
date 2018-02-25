package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 21-Feb-18
 */
public class IntegerAssertTest {

  @Test
  public void isEqualTo() throws Exception {
    assertNotNull(new IntegerAssert(1).isEqualTo(1));
  }

  @Test(expected = AssertionError.class)
  public void isEqualTo_WhenIsNot() throws Exception {
    assertNotNull(new IntegerAssert(1).isEqualTo(2));
  }

  @Test(expected = AssertionError.class)
  public void checkIsNotEqualToOne_WhenIs() throws Exception {
    new IntegerAssert(1).isNotEqualTo(1);
  }

  @Test
  public void checkIsNotEqualToOne_WhenIsNot() throws Exception {
    new IntegerAssert(1).isNotEqualTo(2);
  }
}