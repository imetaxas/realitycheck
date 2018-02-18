package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.Assert.assertNotNull;

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

  @Test
  public void testEqualsIgnoreCase() throws Exception {
    String string1 = "RandomString";
    String string2 = "RANDOMSTRING";

    assertNotNull(checkThat(string1).equalsIgnoreCase(string2));
  }

  @Test(expected = AssertionError.class)
  public void testEqualsIgnoreCase_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).equalsIgnoreCase("");
  }

  @Test
  public void testEquals() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).equals(string));
  }

  @Test(expected = AssertionError.class)
  public void testEquals_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).equals("");
  }

  @Test
  public void testStartsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).startsWith("Random"));
  }

  @Test(expected = AssertionError.class)
  public void testStartsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).startsWith("aaa");
  }

  @Test
  public void testEndsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).endsWith("String"));
  }

  @Test(expected = AssertionError.class)
  public void testEndsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).endsWith("aaa");
  }

  @Test
  public void testContains() throws Exception {
    String string = "RandomString";

    assertNotNull(checkThat(string).contains("String"));
  }

  @Test(expected = AssertionError.class)
  public void testContains_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).contains("aaa");
  }

  @Test
  public void testIsEmpty() throws Exception {
    String string = "";

    assertNotNull(checkThat(string).isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testIsEmpty_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).isEmpty();
  }

  @Test
  public void testMatches() throws Exception {
    String string = "aaaaab";

    assertNotNull(checkThat(string).matches("a*b"));
  }

  @Test(expected = AssertionError.class)
  public void testMatches_WhenIsNot() throws Exception {
    String string = "RandomString";

    checkThat(string).matches("");
  }
}