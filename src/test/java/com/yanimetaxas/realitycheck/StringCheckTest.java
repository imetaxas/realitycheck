package com.yanimetaxas.realitycheck;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringCheckTest {

  @Test
  public void testIsNull() throws Exception {
    String string = null;

    assertNotNull(new StringCheck(string).isNull());
  }

  @Test
  public void testIsNotNull() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).isNotNull());
  }

  @Test
  public void testHasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testHasLength10_False() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).hasLength(10));
  }

  @Test
  public void test_NotNull_HasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).isNotNull().hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testEqualsIgnoreCase_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).equalsIgnoreCase("");
  }

  @Test
  public void testEquals() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).equals(string));
  }

  @Test(expected = AssertionError.class)
  public void testEquals_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).equals("");
  }

  @Test
  public void testStartsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).startsWith("Random"));
  }

  @Test(expected = AssertionError.class)
  public void testStartsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).startsWith("aaa");
  }

  @Test
  public void testEndsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).endsWith("String"));
  }

  @Test(expected = AssertionError.class)
  public void testEndsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).endsWith("aaa");
  }

  @Test
  public void testContains() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).contains("String"));
  }

  @Test(expected = AssertionError.class)
  public void testContains_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).contains("aaa");
  }

  @Test
  public void testDoesNotContain() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringCheck(string).doesNotContain("aa"));
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotContain_WhenIs() throws Exception {
    String string = "RandomString";

    new StringCheck(string).doesNotContain("Random");
  }

  @Test
  public void testIsEmpty() throws Exception {
    String string = "";

    assertNotNull(new StringCheck(string).isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testIsEmpty_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).isEmpty();
  }

  @Test
  public void testIsNotEmpty() throws Exception {
    String string = "aa";

    assertNotNull(new StringCheck(string).isNotEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testIsNotEmpty_WhenIs() throws Exception {
    String string = "";

    new StringCheck(string).isNotEmpty();
  }

  @Test
  public void testMatches() throws Exception {
    String string = "aaaaab";

    assertNotNull(new StringCheck(string).matches("a*b"));
  }

  @Test(expected = AssertionError.class)
  public void testMatches_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringCheck(string).matches("");
  }

  @Test
  public void testDoesNotMatch() throws Exception {
    String string = "aaaaab";

    assertNotNull(new StringCheck(string).doesNotMatch(""));
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotMatch_WhenDoes() throws Exception {
    String string = "aaaaab";

    new StringCheck(string).doesNotMatch("a*b");
  }

  @Test
  public void testDoesNotEqualIgnoreCase() throws Exception {
    String string = "aaaaab";

    new StringCheck(string).doesNotEqualIgnoreCase("a");
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotEqualIgnoreCase_WhenIsNot() throws Exception {
    String string = "aaaaab";

    new StringCheck(string).doesNotEqualIgnoreCase("AAAAAB");
  }

  @Test
  public void testDoesNotEqual() throws Exception {
    String string = "aaaaab";

    new StringCheck(string).doesNotEqual("AAAAAB");
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotEqual_WhenIsNot() throws Exception {
    String string = "aaaaab";

    new StringCheck(string).doesNotEqual("aaaaab");
  }

}