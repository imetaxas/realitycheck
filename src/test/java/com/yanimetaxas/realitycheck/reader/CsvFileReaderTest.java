package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.*;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import java.util.List;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileReaderTest {

  @Test
  public void readCsvFile() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader(new File("sampleA.csv"));
    byte[] lines = csvFileReader.read();

    assertNotNull(lines);
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_NoCsv() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader(new File("test.txt"));
    csvFileReader.read();
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_Empty() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader(new File("test.txt"));
    csvFileReader.read();
  }

  @Test(expected = ValidationException.class)
  public void readCsvFile_Null() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader(null);

    csvFileReader.read();
  }
}