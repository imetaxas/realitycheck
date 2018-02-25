package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceAssertTest {

  @Test
  public void hasSameContentAs() throws Exception {
    String filename = "test.txt";
    assertNotNull(new SystemResourceAssert(filename, null).hasSameContentAs(filename));
  }

  @Test
  public void hasNotSameContentAs() throws Exception {
    String filename1 = "test.txt";
    String filename2 = "empty.csv";
    assertNotNull(new SystemResourceAssert(filename1, null).hasNotSameContentAs(filename2));
  }

  @Test(expected = AssertionError.class)
  public void hasSameContentAs_WhenFilename2NotExists() throws Exception {
    String filename1 = "test.txt";
    String filename2 = "notExists.txt";
    new SystemResourceAssert(filename1, null).hasNotSameContentAs(filename2);
  }
}