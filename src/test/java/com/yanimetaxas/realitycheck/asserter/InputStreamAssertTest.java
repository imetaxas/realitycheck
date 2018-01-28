package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class InputStreamAssertTest {

  @Test
  public void isSameAs_True() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertNotNull(assertThat(new ByteArrayInputStream(bytes)).hasSameContentAs(new ByteArrayInputStream(bytes)));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_False() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertThat(new ByteArrayInputStream(bytes)).hasNotSameContentAs(new ByteArrayInputStream(bytes));
  }

  @Test
  public void isNotSameAs_True() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    assertNotNull(assertThat(new ByteArrayInputStream(bytes1)).hasNotSameContentAs(new ByteArrayInputStream(bytes2)));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    assertThat(new ByteArrayInputStream(bytes1)).hasSameContentAs(new ByteArrayInputStream(bytes2));
  }
}