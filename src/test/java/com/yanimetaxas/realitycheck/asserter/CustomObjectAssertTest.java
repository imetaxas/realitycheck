package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CustomObjectAssertTest {

  @Test
  public void isStringNull() throws Exception {
    CustomObjectAssert asserter = new CustomObjectAssert(new CustomObject(null, 1));
    assertNotNull(asserter.isStringNull());
    assertNotNull(asserter.isIntegerGreaterThanZero());
  }

  @Test(expected = ValidationException.class)
  public void isString_WhenStringNotNull() throws Exception {
    CustomObjectAssert asserter = new CustomObjectAssert(new CustomObject("", 1));
    asserter.isStringNull();
  }

  @Test(expected = ValidationException.class)
  public void isString_WhenIntegerIsNotGreaterThanZero() throws Exception {
    CustomObjectAssert asserter = new CustomObjectAssert(new CustomObject("", -1));
    asserter.isIntegerGreaterThanZero();
  }

}