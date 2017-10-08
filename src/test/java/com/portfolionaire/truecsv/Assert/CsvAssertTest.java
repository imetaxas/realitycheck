package com.portfolionaire.truecsv.Assert;

import static com.portfolionaire.truecsv.Assert.CsvAssert.assertThatCsv;
import static com.portfolionaire.truecsv.Assert.CsvAssert.assertThatFileCsv;
import static org.junit.Assert.*;

import com.portfolionaire.truecsv.tools.CoverageTool;
import com.portfolionaire.truecsv.util.Files;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * Created by imeta on 21-Sep-17.
 */
public class CsvAssertTest {

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