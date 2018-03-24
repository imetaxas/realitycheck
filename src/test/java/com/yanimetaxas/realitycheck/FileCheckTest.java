package com.yanimetaxas.realitycheck;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileCheckTest {

  @Test
  public void testFilePathIsNull() throws Exception {
    String filePath = null;
    FileCheck fileCheck = new FileCheck(filePath, null);

    assertNotNull(fileCheck.isNull());
  }

  @Test
  public void testFilepathExists() throws Exception {
    String filePath = IoUtil.loadResource("test.txt").getPath();
    FileCheck fileCheck = new FileCheck(filePath, null);

    assertNotNull(fileCheck.exists());
  }

  @Test
  public void testFilepathNotExists() throws Exception {
    String filePath = "aaa";
    FileCheck fileCheck = new FileCheck(filePath, null);

    assertNotNull(fileCheck.doesNotExist());
  }

  @Test
  public void testFileIsNull() throws Exception {
    File file = null;
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck);
  }

  @Test
  public void testFileNotExists() throws Exception {
    File file = new File("");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.doesNotExist());
  }

  @Test(expected = AssertionError.class)
  public void testFileNotExists_WhenNotExists() throws Exception {
    File file = IoUtil.loadResource("test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.doesNotExist();
  }

  @Test
  public void testFileNotExists_WhenExists() throws Exception {
    File file = IoUtil.loadResource("test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.exists());
  }

  @Test(expected = AssertionError.class)
  public void testFileExists_WhenFileNotExists() throws Exception {
    File file = new File("");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.exists();
  }

  @Test
  public void testFileIsDirectory() throws Exception {
    File file = IoUtil.loadResource("dir");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.isDirectory());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsDirectory_WhenIsNotDirectory() throws Exception {
    File file = new File("test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.isDirectory();
  }

  @Test
  public void testFileIsNotDirectory() throws Exception {
    File file = new File("test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.isNotDirectory());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsNotDirectory_WhenIsDirectory() throws Exception {
    File file = IoUtil.loadResource("dir");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.isNotDirectory();
  }

  @Test
  public void testFileIsHidden() throws Exception {
    File file = new File("src/test/resources/hidden.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.isHidden());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsHidden_WhenIsNot() throws Exception {
    File file = new File("src/test/resources/test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.isHidden();
  }

  @Test
  public void testFileIsNotHidden() throws Exception {
    File file = new File("src/test/resources/test.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    assertNotNull(fileCheck.isNotHidden());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsHidden_WhenIs() throws Exception {
    File file = new File("src/test/resources/hidden.txt");
    FileCheck fileCheck = new FileCheck(file, null);

    fileCheck.isNotHidden();
  }
}