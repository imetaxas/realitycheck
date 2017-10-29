package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.Reality.assertThatCsv;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.CoverageTool;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvAssertTest {

  @Test
  public void assertThatCsv_WhenCsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(assertThatCsv(csv));
  }

  @Test
  public void assertThatCsv_CsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(assertThatCsv(csv).isNull());
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_CsvIsNull_AssertNotNull() throws Exception {
    String csv = null;

    assertNotNull(assertThatCsv(csv).isNotNull());
  }

  /*@Test
  public void assertThatCsvTest() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    assertNotNull(assertThatCsv(csv));
  }*/
}