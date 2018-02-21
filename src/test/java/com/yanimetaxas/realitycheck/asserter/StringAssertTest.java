package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringAssertTest {

  @Test
  public void testIsNull() throws Exception {
    String string = null;

    assertNotNull(new StringAssert(string).isNull());
  }

  @Test
  public void testIsNotNull() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).isNotNull());
  }

  @Test
  public void testHasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testHasLength10_False() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).hasLength(10));
  }

  @Test
  public void test_NotNull_HasLength12_True() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).isNotNull().hasLength(12));
  }

  @Test(expected = AssertionError.class)
  public void testEqualsIgnoreCase_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).equalsIgnoreCase("");
  }

  @Test
  public void testEquals() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).equals(string));
  }

  @Test(expected = AssertionError.class)
  public void testEquals_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).equals("");
  }

  @Test
  public void testStartsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).startsWith("Random"));
  }

  @Test(expected = AssertionError.class)
  public void testStartsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).startsWith("aaa");
  }

  @Test
  public void testEndsWith() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).endsWith("String"));
  }

  @Test(expected = AssertionError.class)
  public void testEndsWith_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).endsWith("aaa");
  }

  @Test
  public void testContains() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).contains("String"));
  }

  @Test(expected = AssertionError.class)
  public void testContains_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).contains("aaa");
  }

  @Test
  public void testDoesNotContain() throws Exception {
    String string = "RandomString";

    assertNotNull(new StringAssert(string).doesNotContain("aa"));
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotContain_WhenIs() throws Exception {
    String string = "RandomString";

    new StringAssert(string).doesNotContain("Random");
  }

  @Test
  public void testIsEmpty() throws Exception {
    String string = "";

    assertNotNull(new StringAssert(string).isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testIsEmpty_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).isEmpty();
  }

  @Test
  public void testIsNotEmpty() throws Exception {
    String string = "aa";

    assertNotNull(new StringAssert(string).isNotEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testIsNotEmpty_WhenIs() throws Exception {
    String string = "";

    new StringAssert(string).isNotEmpty();
  }

  @Test
  public void testMatches() throws Exception {
    String string = "aaaaab";

    assertNotNull(new StringAssert(string).matches("a*b"));
  }

  @Test(expected = AssertionError.class)
  public void testMatches_WhenIsNot() throws Exception {
    String string = "RandomString";

    new StringAssert(string).matches("");
  }

  @Test
  public void testDoesNotMatch() throws Exception {
    String string = "aaaaab";

    assertNotNull(new StringAssert(string).doesNotMatch(""));
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotMatch_WhenDoes() throws Exception {
    String string = "aaaaab";

    new StringAssert(string).doesNotMatch("a*b");
  }

  @Test
  public void testDoesNotEqualIgnoreCase() throws Exception {
    String string = "aaaaab";

    new StringAssert(string).doesNotEqualIgnoreCase("a");
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotEqualIgnoreCase_WhenIsNot() throws Exception {
    String string = "aaaaab";

    new StringAssert(string).doesNotEqualIgnoreCase("AAAAAB");
  }

  @Test
  public void testDoesNotEqual() throws Exception {
    String string = "aaaaab";

    new StringAssert(string).doesNotEqual("AAAAAB");
  }

  @Test(expected = AssertionError.class)
  public void testDoesNotEqual_WhenIsNot() throws Exception {
    String string = "aaaaab";

    new StringAssert(string).doesNotEqual("aaaaab");
  }

}