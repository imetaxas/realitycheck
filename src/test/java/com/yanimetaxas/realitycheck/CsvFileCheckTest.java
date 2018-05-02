package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileCheckTest {

  @Test(expected = AssertionError.class)
  public void checkCsvFileHasSameContentAsANonCsvFile() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/test.txt";

    new CsvFileCheck(filename1, null).hasSameContentAs(filename2);
  }

  @Test(expected = AssertionError.class)
  public void checkCsvFileHasSameContentAsAnEmptyCsvFile() throws Exception {
    String filename1 = "src/test/resources/sampleA.csv";
    String filename2 = "src/test/resources/empty.csv";

    new CsvFileCheck(filename1, null).hasSameContentAs(filename2);
  }

  @Test(expected = AssertionError.class)
  public void checkCsvFileHasNotSameContentAs() throws Exception {
    File file = IoUtil.loadResource("sampleA.csv");

    new CsvFileCheck(file, null).hasNotSameContentAs(file);
  }

  @Test(expected = AssertionError.class)
  public void checkCsvFileHeaderHasNoDigitsWhenHeaderHas() throws Exception {
    File file = IoUtil.loadResource("sampleA.csv");

    new CsvFileCheck(file, null).headerHasNoDigits();
  }
}
