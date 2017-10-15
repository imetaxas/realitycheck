package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

/**
 * Created by imeta on 14-Oct-17.
 */
public class CsvReaderTest {

  @Test
  public void readCsv() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    CsvReader csvReader = new CsvReader(csv);
    List<String[]> contents = csvReader.read();

    assertEquals(contents.size(), 1);
    assertEquals(contents.get(0)[0], "1");
    assertEquals(contents.get(0)[10], "0.8");
  }

}