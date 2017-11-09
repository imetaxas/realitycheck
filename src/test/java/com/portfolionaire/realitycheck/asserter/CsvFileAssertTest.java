package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.Reality.assertThatFileCsv;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.Reality;
import com.portfolionaire.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * Created by imeta on 28-Oct-17.
 */
public class CsvFileAssertTest {

  @Test
  public void assertThatFileCsv_FilenameIsNull() throws Exception {
    String filename = null;

    assertNotNull(assertThatFileCsv(filename));
  }

  @Test
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    String filename = "";

    assertNotNull(assertThatFileCsv(filename));
  }

  @Test
  public void isSameAs_given_file() throws Exception {
    File file = Files.toFile("sampleA.csv"); //ISO-8859-1

    assertNotNull(assertThatFileCsv(file).hasSameContentAs(file.getName()));
  }

  @Test
  public void isSameAs_given_filename() throws Exception {
    String filename = "sampleA.csv";

    assertNotNull(assertThatFileCsv(filename).hasSameContentAs(filename));
  }

  @Test
  public void assertThatFileCsv_NoFileCsv() throws Exception {
    File file = Files.toFile("test.txt");

    assertNotNull(assertThatFileCsv(file.getName()));
  }

  @Test
  public void assertThatFileCsv_FileIsEmpty() throws Exception {
    File file = Files.toFile("empty.csv");

    assertNotNull(assertThatFileCsv(file));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_not_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "test.txt";

    assertNotNull(assertThatFileCsv(filename1).hasSameContentAs(filename2));
  }

  @Test(expected = AssertionError.class)
  public void isSameAs_given_csv_and_empty_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "empty.csv";

    assertNotNull(assertThatFileCsv(filename1).hasSameContentAs(filename2));
  }

  @Test
  public void isNotSameAs_given_file() throws Exception {
    File file1 = Files.toFile("sampleA.csv");
    File file2 = Files.toFile("sampleB.csv");

    assertNotNull(assertThatFileCsv(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void isNotSameAs_False() throws Exception {
    File file = Files.toFile("sampleA.csv");

    assertNotNull(assertThatFileCsv(file).hasNotSameContentAs(file));
  }

  @Test
  public void isNotSameAs_given_filename() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "sampleB.csv";

    assertNotNull(assertThatFileCsv(filename1).hasNotSameContentAs(filename2));
  }

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = Files.toFile("withHeader.csv");

    assertNotNull(Reality.assertThatFileCsv(file).headerHasNoDigits());
  }

  @Test(expected = AssertionError.class)
  public void headerHasNoDigits_NoHeader() throws Exception {
    File file = Files.toFile("sampleA.csv");

    assertThatFileCsv(file).headerHasNoDigits();
  }
}
