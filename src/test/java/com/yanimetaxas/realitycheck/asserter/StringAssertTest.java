package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringAssertTest {

  @Test
  public void testIsNull() throws Exception {
    String string = null;

    assertNotNull(checkThat(string).isNull());
  }

  @Test
  public void testIsNotNull() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).isNotNull());
  }

  @Test
  public void testHasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testHasLength10_False() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).hasLength(10));
  }

  @Test
  public void test_NotNull_HasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).isNotNull().hasLength(12));
  }
}