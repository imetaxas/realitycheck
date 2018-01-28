package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.*;

import com.yanimetaxas.realitycheck.util.CustomObject;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CustomReadableObjectAssertTest {

  @Test
  public void isStringNull() throws Exception {
    CustomReadableObjectAssert asserter = new CustomReadableObjectAssert(new CustomObject(null, 1));
    assertNotNull(asserter.isStringNull());
    assertNotNull(asserter.isIntegerGreaterThanZero());
  }

}