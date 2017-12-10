package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.Reality.assertThat;
import static com.portfolionaire.realitycheck.Reality.assertThatFile;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileAssertTest {

  @Test
  public void assertThatFile_FilePathIsNull() throws Exception {
    String filePath = null;

    assertNotNull(assertThatFile(filePath).isNull());
  }

  @Test
  public void assertThatFile_FilepathExists() throws Exception {
    String filePath = Files.loadResource("test.txt").getPath();

    assertNotNull(assertThatFile(filePath).exists());
  }

  @Test
  public void assertThatFile_FilepathNotExists() throws Exception {
    String filePath = "aaa";

    assertNotNull(assertThatFile(filePath).notExists());
  }

  @Test
  public void assertThat_FileIsNull() throws Exception {
    File file = null;

    assertNotNull(assertThat(file));
  }

  @Test
  public void assertThatFileCsv_FileExists() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(assertThat(file).exists());
  }

  @Test
  public void assertThatFileCsv_FileNotExists() throws Exception {
    File file = new File("");

    assertNotNull(assertThat(file).notExists());
  }

}