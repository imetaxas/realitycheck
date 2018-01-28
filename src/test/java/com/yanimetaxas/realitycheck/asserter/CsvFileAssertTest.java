package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThatFileCsv;
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

    assertNotNull(Reality.checkThatFileCsv(filename));
  }

  @Test
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    String filename = "";

    assertNotNull(Reality.checkThatFileCsv(filename));
  }

  @Test
  public void isSameAs_given_file() throws Exception {
    File file = Files.loadResource("sampleA.csv"); //ISO-8859-1

    assertNotNull(checkThatFileCsv(file).hasSameContentAs(file.getName()));
  }

  @Test
  public void isSameAs_given_filename() throws Exception {
    String filename = "sampleA.csv";

    assertNotNull(Reality.checkThatFileCsv(filename).hasSameContentAs(filename));
  }

  @Test
  public void assertThatFileCsv_NoFileCsv() throws Exception {
    File file = Files.loadResource("test.txt");

    assertNotNull(Reality.checkThatFileCsv(file.getName()));
  }

  @Test
  public void assertThatFileCsv_FileIsEmpty() throws Exception {
    File file = Files.loadResource("empty.csv");

    assertNotNull(checkThatFileCsv(file));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_not_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "test.txt";

    Reality.checkThatFileCsv(filename1).hasSameContentAs(filename2);
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_empty_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "empty.csv";

    Reality.checkThatFileCsv(filename1).hasSameContentAs(filename2);
  }

  @Test
  public void isNotSameAs_given_file() throws Exception {
    File file1 = Files.loadResource("sampleA.csv");
    File file2 = Files.loadResource("sampleB.csv");

    assertNotNull(checkThatFileCsv(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    File file = Files.loadResource("sampleA.csv");

    checkThatFileCsv(file).hasNotSameContentAs(file);
  }

  @Test
  public void isNotSameAs_given_filename() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "sampleB.csv";

    assertNotNull(Reality.checkThatFileCsv(filename1).hasNotSameContentAs(filename2));
  }

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = Files.loadResource("withHeader.csv");

    assertNotNull(Reality.checkThatFileCsv(file).headerHasNoDigits());
  }

  @Test(expected = AssertionError.class)
  public void headerHasNoDigits_NoHeader() throws Exception {
    File file = Files.loadResource("sampleA.csv");

    checkThatFileCsv(file).headerHasNoDigits();
  }
}
