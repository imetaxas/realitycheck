package com.yanimetaxas.realitycheck;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 26-Feb-18
 */
public class AbstractReadableCheckTest {

  @Test(expected = AssertionError.class)
  public void getAssertFromType() throws Exception {
    CustomReadableTestObject customReadableObject = new CustomReadableTestObject();
    new CustomReadableTestObjectAssert(customReadableObject);
  }
}