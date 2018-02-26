package com.yanimetaxas.realitycheck.custom;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 26-Feb-18
 */
public class AbstractCustomObjectTest {

  @Test(expected = AssertionError.class)
  public void getAssertFromType() throws Exception {
    CustomReadableTestObject customReadableObject = new CustomReadableTestObject();
    customReadableObject.getAssertFromType();
  }

}