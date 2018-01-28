package com.yanimetaxas.realitycheck.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.yanimetaxas.realitycheck.tools.CoverageTool;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class IoUtilTest {

  @Test
  public void read() throws Exception {
    File file = IoUtil.toFileOrNull("test.txt");

    assertNotNull(file);
  }

  @Test
  public void areInputStreamsEqual_True() throws Exception {
    FileInputStream fis1 = new FileInputStream(IoUtil.loadResourceOrNull("test.txt"));
    FileInputStream fis2 = new FileInputStream(IoUtil.loadResourceOrNull("test.txt"));

    assertTrue(IOUtils.contentEquals(fis1, fis2));
  }

  @Test
  public void areByteArrayInputStreamAndInputStreamsEqual_True() throws Exception {
    File resource = IoUtil.loadResourceOrThrow(IoUtil.loadResourceOrNull("test.txt").getName());
    FileInputStream fis2 = new FileInputStream(IoUtil.loadResourceOrNull("test.txt"));

    byte[] bytes = IOUtils.toByteArray(new java.io.FileReader(resource));
    assertTrue(IOUtils.contentEquals(new ByteArrayInputStream(new String(bytes).getBytes()), fis2));
  }

  @Test
  public void areInputStreamsEqual_False() throws Exception {
    FileInputStream fis1 = new FileInputStream(IoUtil.loadResourceOrNull("test.txt"));
    FileInputStream fis2 = new FileInputStream(IoUtil.loadResourceOrNull("empty.csv"));

    assertFalse(IOUtils.contentEquals(fis1, fis2));
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(IoUtil.class);
  }
}