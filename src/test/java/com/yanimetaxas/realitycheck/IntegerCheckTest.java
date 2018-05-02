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
  public void isEqualToWhenIsNot() throws Exception {
    assertNotNull(new IntegerCheck(1).isEqualTo(2));
  }

  @Test(expected = AssertionError.class)
  public void checkIsNotEqualToOneWhenIs() throws Exception {
    new IntegerCheck(1).isNotEqualTo(1);
  }

  @Test
  public void checkIsNotEqualToOneWhenIsNot() throws Exception {
    new IntegerCheck(1).isNotEqualTo(2);
  }
}