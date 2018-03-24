package com.yanimetaxas.realitycheck;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 21-Feb-18
 */
public class IntegerCheckTest {

  @Test
  public void isEqualTo() throws Exception {
    assertNotNull(new IntegerCheck(1).isEqualTo(1));
  }

  @Test(expected = AssertionError.class)
  public void isEqualTo_WhenIsNot() throws Exception {
    assertNotNull(new IntegerCheck(1).isEqualTo(2));
  }

  @Test(expected = AssertionError.class)
  public void checkIsNotEqualToOne_WhenIs() throws Exception {
    new IntegerCheck(1).isNotEqualTo(1);
  }

  @Test
  public void checkIsNotEqualToOne_WhenIsNot() throws Exception {
    new IntegerCheck(1).isNotEqualTo(2);
  }
}