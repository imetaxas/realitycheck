package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvAssertTest {

  @Test
  public void assertThatCsv_WhenCsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(new CsvAssert(csv, null));
  }

  @Test
  public void assertThatCsv_CsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(new CsvAssert(csv, null).isNull());
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_CsvIsNull_AssertNotNull() throws Exception {
    String csv = null;

    new CsvAssert(csv, null).isNotNull();
  }

  @Test
  public void assertThatCsv_hasSameContentAs() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    assertNotNull(new CsvAssert(csv, null).hasSameContentAs(csv));
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_hasNotSameContentAs() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    new CsvAssert(csv, null).hasNotSameContentAs(csv);
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_WhenHeaderNotExists() throws Exception {
    String csv = "";

    new CsvAssert(csv, null).headerHasNoDigits();
  }


}