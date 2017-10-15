package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.*;

import com.portfolionaire.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * Created by imeta on 14-Oct-17.
 */
public class CsvFileReaderTest {

  @Test
  public void readCsvFile() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader("sampleA.csv");
    byte[] contents = csvFileReader.read();

    assertNotNull(contents[0]);
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_Empty() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader("test.txt");
    byte[] contents = csvFileReader.read();

    assertNotNull(contents[0]);
  }
}