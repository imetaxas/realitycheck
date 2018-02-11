package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertEquals;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import java.util.List;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvReaderTest {

  @Test
  public void readCsv() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    CsvReader csvReader = new CsvReader(csv);
    List contents = csvReader.read();

    assertEquals(contents.size(), 1);
    assertEquals(((String)contents.get(0)).split(",")[0], "1");
    assertEquals(((String)contents.get(0)).split(",")[10], "0.8");
  }

  @Test(expected = ReaderException.class)
  public void readCsv_csvIsNull() throws Exception {
    CsvReader csvReader = new CsvReader(null);

    csvReader.read();
  }

  @Test
  public void readCsv_csvIsEmpty() throws Exception {
    String csv = "";

    CsvReader csvReader = new CsvReader(csv);
    List contents = csvReader.read();

    assertEquals(contents.size(), 0);
  }

  @Test
  public void readCsv_csvIsNotCsv() throws Exception {
    String csv = "aaaa";

    CsvReader csvReader = new CsvReader(csv);
    List contents = csvReader.read();

    assertEquals(contents.size(), 1);
  }
}