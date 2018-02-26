package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.custom.CustomReadableTestObject;
import com.yanimetaxas.realitycheck.custom.CustomReadableTestObjectAssert;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 26-Feb-18
 */
public class AbstractReadableAssertTest {

  @Test(expected = AssertionError.class)
  public void getAssertFromType() throws Exception {
    CustomReadableTestObject customReadableObject = new CustomReadableTestObject();
    new CustomReadableTestObjectAssert(customReadableObject);
  }
}