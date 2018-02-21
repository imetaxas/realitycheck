package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 21-Feb-18
 */
public class IntegerAssertTest {

  @Test
  public void isOne() throws Exception {
    assertNotNull(new IntegerAssert(1).isEqualTo(1));
  }

  @Test(expected = AssertionError.class)
  public void checkIsOne_WhenIsNot() throws Exception {
    new IntegerAssert(1).isNotEqualTo(1);
  }
}