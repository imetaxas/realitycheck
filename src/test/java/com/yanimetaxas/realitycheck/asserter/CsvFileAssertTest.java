package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThatCsvFile;
import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.Reality;
import com.yanimetaxas.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileAssertTest {

  @Test
  public void assertThatFileCsv_FilenameIsNull() throws Exception {
    String filename = null;

    assertNotNull(Reality.checkThatCsvFile(filename));
  }

  @Test
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    String filename = "";

    assertNotNull(Reality.checkThatCsvFile(filename));
  }

  @Test
  public void isSameAs_given_file() throws Exception {
    File file = Files.loadResource("sampleA.csv");

    assertNotNull(checkThatCsvFile(file).hasSameContentAs(file.getAbsolutePath()));
  }

  @Test
  public void isSameAs_given_filepath() throws Exception {
    String filepath = "src/test/resources/sampleA.csv";

    assertNotNull(Reality.checkThatCsvFile(filepath).hasSameContentAs(filepath));
  }

  @Test
  public void assertThatFileCsv_NoFileCsv() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(Reality.checkThatCsvFile(file.getName()));
  }

  @Test
  public void assertThatFileCsv_FileIsEmpty() throws Exception {
    File file = Files.loadResource("empty.csv");

    assertNotNull(checkThatCsvFile(file));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_not_csv() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/test.txt";

    Reality.checkThatCsvFile(filename1).hasSameContentAs(filename2);
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_empty_csv() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/empty.csv";

    Reality.checkThatCsvFile(filename1).hasSameContentAs(filename2);
  }

  @Test
  public void isNotSameAs_given_file() throws Exception {
    File file1 = Files.loadResource("sampleA.csv");
    File file2 = Files.loadResource("sampleB.csv");

    assertNotNull(checkThatCsvFile(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    File file = Files.loadResource("sampleA.csv");

    checkThatCsvFile(file).hasNotSameContentAs(file);
  }

  @Test
  public void isNotSameAs_given_filename() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/sampleB.csv";

    assertNotNull(Reality.checkThatCsvFile(filename1).hasNotSameContentAs(filename2));
  }

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = Files.loadResource("withHeader.csv");

    assertNotNull(Reality.checkThatCsvFile(file).headerHasNoDigits());
  }

  @Test(expected = AssertionError.class)
  public void headerHasNoDigits_NoHeader() throws Exception {
    File file = Files.loadResource("sampleA.csv");

    checkThatCsvFile(file).headerHasNoDigits();
  }
}
