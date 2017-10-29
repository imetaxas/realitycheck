package com.portfolionaire.realitycheck.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.portfolionaire.realitycheck.tools.CoverageTool;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class IoUtilTest {

  @Test
  public void read() throws Exception {
    Optional<File> file = IoUtil.toFile("test.txt");

    assertNotNull(file);
  }

  @Test
  public void areInputStreamsEqual_True() throws Exception {
    FileInputStream fis1 = new FileInputStream(IoUtil.toFile("test.txt").get());
    FileInputStream fis2 = new FileInputStream(IoUtil.toFile("test.txt").get());

    assertTrue(IoUtil.areInputStreamsEqual(fis1, fis2));
  }

  @Test
  public void areByteArrayInputStreamAndInputStreamsEqual_True() throws Exception {
    File resource = IoUtil.loadResource(IoUtil.toFile("test.txt").get().getName());
    FileInputStream fis2 = new FileInputStream(IoUtil.toFile("test.txt").get());

    byte[] bytes = IOUtils.toByteArray(new java.io.FileReader(resource));
    assertTrue(IoUtil.areInputStreamsEqual(new ByteArrayInputStream(new String(bytes).getBytes()), fis2));
  }

  @Test
  public void areInputStreamsEqual_False() throws Exception {
    FileInputStream fis1 = new FileInputStream(IoUtil.toFile("test.txt").get());
    FileInputStream fis2 = new FileInputStream(IoUtil.toFile("empty.csv").get());

    assertFalse(IoUtil.areInputStreamsEqual(fis1, fis2));
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testProtectedConstructor(IoUtil.class);
  }
}