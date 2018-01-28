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
  public void assertThatFile_FilePathIsNull() throws Exception {
    String filePath = null;

    assertNotNull(checkThatFile(filePath).isNull());
  }

  @Test
  public void assertThatFile_FilepathExists() throws Exception {
    String filePath = Files.loadResource("test.txt").getPath();

    assertNotNull(checkThatFile(filePath).exists());
  }

  @Test
  public void assertThatFile_FilepathNotExists() throws Exception {
    String filePath = "aaa";

    assertNotNull(checkThatFile(filePath).notExists());
  }

  @Test
  public void assertThat_FileIsNull() throws Exception {
    File file = null;

    assertNotNull(checkThat(file));
  }

  @Test
  public void assertThatFileCsv_FileExists() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(checkThat(file).exists());
  }

  @Test
  public void assertThatFileCsv_FileNotExists() throws Exception {
    File file = new File("");

    assertNotNull(checkThat(file).notExists());
  }

}