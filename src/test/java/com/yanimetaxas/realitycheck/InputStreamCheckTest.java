package com.yanimetaxas.realitycheck;

import java.io.ByteArrayInputStream;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class InputStreamCheckTest {

  @Test(expected = AssertionError.class)
  public void checkHasSameContentAsWhenHasNot() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    new InputStreamCheck(new ByteArrayInputStream(bytes1)).hasSameContentAs(new ByteArrayInputStream(bytes2));
  }
}