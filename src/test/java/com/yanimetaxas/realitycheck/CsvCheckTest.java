package com.yanimetaxas.realitycheck;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvCheckTest {

  @Test(expected = AssertionError.class)
  public void checkCsvIsNotNullWhenCsvIsNull() throws Exception {
    String csv = null;

    new CsvCheck(csv, null).isNotNull();
  }

  @Test(expected = AssertionError.class)
  public void checkCsvWhenHasNotSameContentAs() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    new CsvCheck(csv, null).hasNotSameContentAs(csv);
  }

  @Test(expected = AssertionError.class)
  public void checkCsvWhenHeaderNotExists() throws Exception {
    String csv = "";

    new CsvCheck(csv, null).headerHasNoDigits();
  }
}