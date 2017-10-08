package com.portfolionaire.truecsv.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.portfolionaire.truecsv.exception.ValidationException;
import com.portfolionaire.truecsv.tools.CoverageTool;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Test;

/**
 * Created by imeta on 24-Sep-17.
 */
public class FilesTest {

  @Test
  public void readFile() throws Exception {
    List<String> contents = Files.readFile("test.txt");

    assertNotNull(contents);
  }

  @Test
  public void read() throws Exception {
    File file = Files.toFile("test.txt");

    assertNotNull(file);
  }

  @Test
  public void readCsvFile() throws Exception {
    List<String[]> contents = Files.readCsvFile("sampleA.csv");

    assertNotNull(contents);
  }

  @Test
  public void readCsv() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";
    String[] contents = Files.readCsv(csv);

    assertEquals(contents.length, 10);
    assertEquals(contents[0], "1");
    assertEquals(contents[9], "0.8");
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(Files.class);
  }
}