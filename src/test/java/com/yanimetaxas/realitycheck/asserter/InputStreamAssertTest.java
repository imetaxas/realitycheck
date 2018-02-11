package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class InputStreamAssertTest {

  @Test
  public void isSameAs_True() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertNotNull(checkThat(new ByteArrayInputStream(bytes)).hasSameContentAs(new ByteArrayInputStream(bytes)));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_False() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    checkThat(new ByteArrayInputStream(bytes)).hasNotSameContentAs(new ByteArrayInputStream(bytes));
  }

  @Test
  public void isNotSameAs_True() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    assertNotNull(checkThat(new ByteArrayInputStream(bytes1)).hasNotSameContentAs(new ByteArrayInputStream(bytes2)));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    checkThat(new ByteArrayInputStream(bytes1)).hasSameContentAs(new ByteArrayInputStream(bytes2));
  }
}