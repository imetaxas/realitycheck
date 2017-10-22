package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.CsvAssert.assertThatCsv;
import static com.portfolionaire.realitycheck.asserter.CsvAssert.assertThatFileCsv;
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
public class CsvAssertTest {

  @Test(expected = ValidationException.class)
  public void assertThatFileCsv_FilenameIsNull() throws Exception {
    String filename = null;

    assertThatFileCsv(filename);
  }

  @Test(expected = AssertionError.class)
  public void assertThatFileCsv_FilenameIsEmpty() throws Exception {
    String filename = "";

    assertThatFileCsv(filename);
  }

  @Test
  public void isSameAs_given_file() throws Exception {
    File file = Files.toFile("sampleA.csv");

    assertNotNull(assertThatFileCsv(file).isSameAs(file));
  }

  @Test
  public void isSameAs_given_filename() throws Exception {
    String filename = "sampleA.csv";

    assertNotNull(assertThatFileCsv(filename).isSameAs(filename));
  }

  @Test(expected = AssertionError.class)
  public void assertThatFileCsv_NoFileCsv() throws Exception {
    File file = Files.toFile("test.txt");

    assertNotNull(assertThatFileCsv(file));
  }

  @Test(expected = AssertionError.class)
  public void assertThatFileCsv_FileIsEmpty() throws Exception {
    File file = Files.toFile("empty.csv");

    assertNotNull(assertThatFileCsv(file));
  }

  @Test(expected = Exception.class)
  public void isSameAs_given_csv_and_not_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "test.txt";

    assertNotNull(assertThatFileCsv(filename1).isSameAs(filename2));
  }

  @Test(expected = Exception.class)
  public void isSameAs_given_csv_and_empty_csv() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "empty.csv";

    assertNotNull(assertThatFileCsv(filename1).isSameAs(filename2));
  }

  @Test
  public void isNotSameAs_given_file() throws Exception {
    File file1 = Files.toFile("sampleA.csv");
    File file2 = Files.toFile("sampleB.csv");

    assertNotNull(assertThatFileCsv(file1).isNotSameAs(file2));
  }

  @Test(expected = Exception.class)
  public void isNotSameAs_False() throws Exception {
    File file = Files.toFile("sampleA.csv");

    assertNotNull(assertThatFileCsv(file).isNotSameAs(file));
  }

  @Test
  public void isNotSameAs_given_filename() throws Exception {
    String filename1 = "sampleA.csv";
    String filename2 = "sampleB.csv";

    assertNotNull(assertThatFileCsv(filename1).isNotSameAs(filename2));
  }

  @Test
  public void assertThatCsvTest() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    assertNotNull(assertThatCsv(csv));
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(CsvAssert.class);
  }
}