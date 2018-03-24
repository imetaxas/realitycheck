package com.yanimetaxas.realitycheck;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileCheckTest {

  @Test
  public void assertThatFileCsv_FilenameIsNull() throws Exception {
    String filename = null;

    assertNotNull(new CsvFileCheck(filename, null));
  }

  @Test
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    String filename = "";

    assertNotNull(new CsvFileCheck(filename, null));
  }

  @Test
  public void isSameAs_given_file() throws Exception {
    File file = IoUtil.loadResource("sampleA.csv");

    assertNotNull(new CsvFileCheck(file, null).hasSameContentAs(file.getAbsolutePath()));
  }

  @Test
  public void isSameAs_given_filepath() throws Exception {
    String filepath = "src/test/resources/sampleA.csv";

    assertNotNull(new CsvFileCheck(filepath, null).hasSameContentAs(filepath));
  }

  @Test
  public void assertThatFileCsv_NoFileCsv() throws Exception {
    File file = IoUtil.loadResource("test.txt");

    assertNotNull(new CsvFileCheck(file.getName(), null));
  }

  @Test
  public void assertThatFileCsv_FileIsEmpty() throws Exception {
    File file = IoUtil.loadResource("empty.csv");

    assertNotNull(new CsvFileCheck(file, null));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_not_csv() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/test.txt";

    new CsvFileCheck(filename1, null).hasSameContentAs(filename2);
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_empty_csv() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/empty.csv";

    new CsvFileCheck(filename1, null).hasSameContentAs(filename2);
  }

  @Test
  public void isNotSameAs_given_file() throws Exception {
    File file1 = IoUtil.loadResource("sampleA.csv");
    File file2 = IoUtil.loadResource("sampleB.csv");

    assertNotNull(new CsvFileCheck(file1, null).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    File file = IoUtil.loadResource("sampleA.csv");

    new CsvFileCheck(file, null).hasNotSameContentAs(file);
  }

  @Test
  public void isNotSameAs_given_filename() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/sampleB.csv";

    assertNotNull(new CsvFileCheck(filename1, null).hasNotSameContentAs(filename2));
  }

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = IoUtil.loadResource("withHeader.csv");

    assertNotNull(new CsvFileCheck(file, null).headerHasNoDigits());
  }

  @Test(expected = AssertionError.class)
  public void headerHasNoDigits_NoHeader() throws Exception {
    File file = IoUtil.loadResource("sampleA.csv");

    new CsvFileCheck(file, null).headerHasNoDigits();
  }
}