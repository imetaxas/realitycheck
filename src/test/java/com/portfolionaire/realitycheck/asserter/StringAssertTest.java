package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.Reality.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringAssertTest {

  @Test
  public void testIsNull() throws Exception {
    String string = null;

    assertNotNull(assertThat(string).isNull());
  }

  @Test
  public void testIsNotNull() throws Exception {
    String string = "RandomString";

    assertNotNull(assertThat(string).isNotNull());
  }

  @Test
  public void testHasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(assertThat(string).hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testHasLength10_False() throws Exception {
    String string = "RandomString";

    assertNotNull(assertThat(string).hasLength(10));
  }
}