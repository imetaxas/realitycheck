package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.Reality.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import org.junit.Test;

/**
 * Created by imeta on 09-Nov-17.
 */
public class InputStreamAssertTest {

  @Test
  public void isSameAs() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertNotNull(assertThat(new ByteArrayInputStream(bytes)).hasSameContentAs(new ByteArrayInputStream(bytes)));
  }

  @Test
  public void isNotSameAs() throws Exception {
    byte[] bytes1 = "RandomString".getBytes();
    byte[] bytes2 = "DifferentRandomString".getBytes();
    assertNotNull(assertThat(new ByteArrayInputStream(bytes1)).hasNotSameContentAs(new ByteArrayInputStream(bytes2)));
  }

}