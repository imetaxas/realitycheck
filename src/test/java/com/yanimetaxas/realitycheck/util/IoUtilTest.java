package com.yanimetaxas.realitycheck.util;

import static org.junit.Assert.assertFalse;
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
    File file = IoUtil.toFile("test.txt");

    assertNotNull(file);
  }

  @Test
  public void testToFileOrNull_WhenFileIsNull() throws Exception {
    File file = IoUtil.toFile(null);

    assertNull(file);
  }

  @Test
  public void testLoadResourceOrNull_WhenResourceExists() throws Exception {
    File file = IoUtil.loadResource("test.txt");

    assertNotNull(file);
  }

  @Test
  public void testLoadResourceOrNull_WhenResourceNotExists() throws Exception {
    File file = IoUtil.loadResource("test");

    assertFalse(file.exists());
  }

  @Test
  public void testReadResource_WhenFilenameExists() throws Exception {
    byte[] bytes = IoUtil.readResource("test.txt");

    assertNotNull(bytes);
  }

  @Test(expected = AssertionError.class)
  public void testReadResource_WhenFilenameNotExists() throws Exception {
    IoUtil.readResource("test");
  }

  @Test(expected = AssertionError.class)
  public void testLoadResource_WhenFilenameIsNull() throws Exception {
    String filename = null;
    IoUtil.readResource(filename);
  }

  @Test(expected = AssertionError.class)
  public void testLoadResource_WhenFileIsNull() throws Exception {
    File file = null;
    IoUtil.readResource(file);
  }

  @Test
  public void testReadFile_WhenFilenameExists() throws Exception {
    byte[] bytes = IoUtil.readFile("src/test/resources/test.txt");

    assertNotNull(bytes);
  }

  @Test(expected = AssertionError.class)
  public void testReadFile_WhenFilenameNotExists() throws Exception {
    IoUtil.readFile("test");
  }

  @Test
  public void testContentEquals() throws Exception {
    byte[] bytes1 = IoUtil.readResource("test.txt");
    byte[] bytes2 = IoUtil.readResource("test.txt");

    assertTrue(IoUtil.contentEquals(new ByteArrayInputStream(bytes1), new ByteArrayInputStream(bytes2)));
  }

  @Test(expected = AssertionError.class)
  public void testContentEquals_WhenInputStreamIsInvalid() throws Exception {
    byte[] bytes1 = IoUtil.readResource("test.txt");

    IoUtil.contentEquals(new ByteArrayInputStream(bytes1), null);
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(IoUtil.class);
  }
}