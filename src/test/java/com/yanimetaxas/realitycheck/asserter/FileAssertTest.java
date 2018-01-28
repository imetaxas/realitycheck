package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.assertThat;
import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.tools.Files;
import com.yanimetaxas.realitycheck.Reality;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileAssertTest {

  @Test
  public void assertThatFile_FilePathIsNull() throws Exception {
    String filePath = null;

    assertNotNull(Reality.assertThatFile(filePath).isNull());
  }

  @Test
  public void assertThatFile_FilepathExists() throws Exception {
    String filePath = Files.loadResource("test.txt").getPath();

    assertNotNull(Reality.assertThatFile(filePath).exists());
  }

  @Test
  public void assertThatFile_FilepathNotExists() throws Exception {
    String filePath = "aaa";

    assertNotNull(Reality.assertThatFile(filePath).notExists());
  }

  @Test
  public void assertThat_FileIsNull() throws Exception {
    File file = null;

    assertNotNull(Reality.assertThat(file));
  }

  @Test
  public void assertThatFileCsv_FileExists() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(Reality.assertThat(file).exists());
  }

  @Test
  public void assertThatFileCsv_FileNotExists() throws Exception {
    File file = new File("");

    assertNotNull(Reality.assertThat(file).notExists());
  }

}