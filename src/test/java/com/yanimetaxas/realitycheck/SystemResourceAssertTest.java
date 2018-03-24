package com.yanimetaxas.realitycheck;

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
    assertNotNull(new SystemResourceCheck(filename, null).hasSameContentAs(filename));
  }

  @Test
  public void hasNotSameContentAs() throws Exception {
    String filename1 = "test.txt";
    String filename2 = "empty.csv";
    assertNotNull(new SystemResourceCheck(filename1, null).hasNotSameContentAs(filename2));
  }

  @Test(expected = AssertionError.class)
  public void hasSameContentAs_WhenFilename2NotExists() throws Exception {
    String filename1 = "test.txt";
    String filename2 = "notExists.txt";
    new SystemResourceCheck(filename1, null).hasNotSameContentAs(filename2);
  }
}