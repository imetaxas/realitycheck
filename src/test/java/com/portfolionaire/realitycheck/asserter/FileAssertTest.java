package com.portfolionaire.realitycheck.asserter;

//import static com.portfolionaire.realitycheck.asserter.CsvAssert.assertThatFileCsv;
//import static com.portfolionaire.realitycheck.asserter.FileAssert.assertThat;
import static com.portfolionaire.realitycheck.asserter.Reality.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.tools.CoverageTool;
import com.portfolionaire.realitycheck.tools.Files;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileAssertTest {

  @Test(expected = ValidationException.class)
  public void assertThatFileCsv_FilenameIsNull() throws Exception {
    File file = null;

    assertNotNull(assertThat(file));
  }

  @Test(expected = ValidationException.class)
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    File file = new File("");

    assertNotNull(assertThat(file));
  }

  @Test(expected = ValidationException.class)
  public void assertThatFileCsv_FilenameIsNonFile() throws Exception {
    File file = new File("aaa");

    assertNotNull(assertThat(file));
  }

  @Test
  public void assertThat_isFile() throws Exception {
    File file = Files.toFile("test.txt");

    assertNotNull(assertThat(file));
  }

  @Test(expected = ValidationException.class)
  public void assertThat_FileIsNull() throws Exception {
    File file = null;

    assertNotNull(assertThat(file));
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testProtectedConstructor(FileAssert.class);
  }
}