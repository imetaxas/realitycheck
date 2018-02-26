package com.yanimetaxas.realitycheck.validator;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileValidatorTest {

  @Test
  public void readCsvFile() throws Exception {
    CsvFileValidator csvFileValidator = new CsvFileValidator(new File("src/test/resources/sampleA.csv"));
    byte[] lines = csvFileValidator.validate();

    assertNotNull(lines);
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_NoCsv() throws Exception {
    CsvFileValidator csvFileValidator = new CsvFileValidator(new File("test.txt"));
    csvFileValidator.validate();
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_Empty() throws Exception {
    CsvFileValidator csvFileValidator = new CsvFileValidator(new File("test.txt"));
    csvFileValidator.validate();
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_Null() throws Exception {
    CsvFileValidator csvFileValidator = new CsvFileValidator(null);

    csvFileValidator.validate();
  }
}