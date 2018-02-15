package com.yanimetaxas.realitycheck.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.yanimetaxas.realitycheck.tools.CoverageTool;
import java.io.ByteArrayInputStream;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class IoUtilTest {

  @Test
  public void testToFileOrNull_WhenFileExists() throws Exception {
    File file = IoUtil.toFileOrNull("test.txt");

    assertNotNull(file);
  }

  @Test
  public void testToFileOrNull_WhenFileIsNull() throws Exception {
    File file = IoUtil.toFileOrNull(null);

    assertNull(file);
  }

  @Test
  public void testLoadResourceOrNull_WhenResourceExists() throws Exception {
    File file = IoUtil.loadResourceOrNull("test.txt");

    assertNotNull(file);
  }

  @Test
  public void testLoadResourceOrNull_WhenResourceNotExists() throws Exception {
    File file = IoUtil.loadResourceOrNull("test");

    assertNull(file);
  }

  @Test
  public void testReadFile_WhenFilenameExists() throws Exception {
    byte[] bytes = IoUtil.readFile("test.txt");

    assertNotNull(bytes);
  }

  @Test(expected = AssertionError.class)
  public void testReadFile_WhenFilenameNotExists() throws Exception {
    IoUtil.readFile("test");
  }

  @Test
  public void testContentEquals() throws Exception {
    byte[] bytes1 = IoUtil.readFile("test.txt");
    byte[] bytes2 = IoUtil.readFile("test.txt");

    assertTrue(IoUtil.contentEquals(new ByteArrayInputStream(bytes1), new ByteArrayInputStream(bytes2)));
  }

  @Test(expected = AssertionError.class)
  public void testContentEquals_WhenInputStreamIsInvalid() throws Exception {
    byte[] bytes1 = IoUtil.readFile("test.txt");
    byte[] bytes2 = IoUtil.readFile("test.txt");

    IoUtil.contentEquals(new ByteArrayInputStream(bytes1), null);
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(IoUtil.class);
  }
}