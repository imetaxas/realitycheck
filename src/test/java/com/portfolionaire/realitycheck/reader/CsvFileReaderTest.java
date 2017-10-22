package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.*;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;
import java.util.List;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileReaderTest {

  @Test
  public void readCsvFile() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader("sampleA.csv");
    List<String> lines = csvFileReader.read();

    assertNotNull(lines);
  }

  @Test
  public void readCsvFile_NoCsv() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader("test.txt");
    List<String> lines = csvFileReader.read();

    assertNotNull(lines);
  }

  @Test
  public void readCsvFile_Empty() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader("test.txt");
    List<String> lines = csvFileReader.read();

    assertNotNull(lines);
  }

  @Test(expected = ReaderException.class)
  public void readCsvFile_Null() throws Exception {
    CsvFileReader csvFileReader = new CsvFileReader(null);

    csvFileReader.read();
  }
}