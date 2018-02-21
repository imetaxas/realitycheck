package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileAssertTest {

  @Test
  public void testFilePathIsNull() throws Exception {
    String filePath = null;
    FileAssert fileAssert = new FileAssert(filePath, null);

    assertNotNull(fileAssert.isNull());
  }

  @Test
  public void testFilepathExists() throws Exception {
    String filePath = Files.loadResource("test.txt").getPath();
    FileAssert fileAssert = new FileAssert(filePath, null);

    assertNotNull(fileAssert.exists());
  }

  @Test
  public void testFilepathNotExists() throws Exception {
    String filePath = "aaa";
    FileAssert fileAssert = new FileAssert(filePath, null);

    assertNotNull(fileAssert.doesNotExist());
  }

  @Test
  public void testFileIsNull() throws Exception {
    File file = null;
    FileAssert fileAssert = new FileAssert(file, null);

    assertNotNull(fileAssert);
  }

  @Test
  public void testFileNotExists() throws Exception {
    File file = new File("");
    FileAssert fileAssert = new FileAssert(file, null);

    assertNotNull(fileAssert.doesNotExist());
  }

  @Test(expected = AssertionError.class)
  public void testFileNotExists_WhenNotExists() throws Exception {
    File file = Files.loadResource("test.txt");
    FileAssert fileAssert = new FileAssert(file, null);

    fileAssert.doesNotExist();
  }

  @Test
  public void testFileNotExists_WhenExists() throws Exception {
    File file = Files.loadResource("test.txt");
    FileAssert fileAssert = new FileAssert(file, null);

    assertNotNull(fileAssert.exists());
  }

  @Test(expected = AssertionError.class)
  public void testFileExists_WhenFileNotExists() throws Exception {
    File file = new File("");
    FileAssert fileAssert = new FileAssert(file, null);

    fileAssert.exists();
  }

  @Test
  public void testFileIsDirectory() throws Exception {
    File file = Files.loadResource("dir");
    FileAssert fileAssert = new FileAssert(file, null);

    assertNotNull(fileAssert.isDirectory());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsDirectory_WhenIsNotDirectory() throws Exception {
    File file = new File("test.txt");
    FileAssert fileAssert = new FileAssert(file, null);

    fileAssert.isDirectory();
  }

  @Test
  public void testFileIsNotDirectory() throws Exception {
    File file = new File("test.txt");
    FileAssert fileAssert = new FileAssert(file, null);

    assertNotNull(fileAssert.isNotDirectory());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsNotDirectory_WhenIsDirectory() throws Exception {
    File file = Files.loadResource("dir");
    FileAssert fileAssert = new FileAssert(file, null);

    fileAssert.isNotDirectory();
  }
}