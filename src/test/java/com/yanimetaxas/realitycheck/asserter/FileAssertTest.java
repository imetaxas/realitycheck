package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static com.yanimetaxas.realitycheck.Reality.checkThatFile;
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

    assertNotNull(checkThatFile(filePath).isNull());
  }

  @Test
  public void testFilepathExists() throws Exception {
    String filePath = Files.loadResource("test.txt").getPath();

    assertNotNull(checkThatFile(filePath).exists());
  }

  @Test
  public void testFilepathNotExists() throws Exception {
    String filePath = "aaa";

    assertNotNull(checkThatFile(filePath).doesNotExist());
  }

  @Test
  public void testFileIsNull() throws Exception {
    File file = null;

    assertNotNull(checkThat(file));
  }

  @Test
  public void testFileNotExists() throws Exception {
    File file = new File("");

    assertNotNull(checkThat(file).doesNotExist());
  }

  @Test(expected = AssertionError.class)
  public void testFileNotExists_WhenNotExists() throws Exception {
    File file = Files.loadResource("test.txt");

    checkThat(file).doesNotExist();
  }

  @Test
  public void testFileNotExists_WhenExists() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(checkThat(file).exists());
  }

  @Test(expected = AssertionError.class)
  public void testFileExists_WhenFileNotExists() throws Exception {
    File file = new File("");

    checkThat(file).exists();
  }

  @Test
  public void testFileIsDirectory() throws Exception {
    File file = Files.loadResource("dir");

    assertNotNull(checkThat(file).isDirectory());
  }

  @Test(expected = AssertionError.class)
  public void testFileIsDirectory_WhenIsNotDirectory() throws Exception {
    File file = new File("test.txt");

    checkThat(file).isDirectory();
  }
}