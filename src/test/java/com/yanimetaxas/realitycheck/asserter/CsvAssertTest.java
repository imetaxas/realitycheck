package com.yanimetaxas.realitycheck.asserter;

import static com.yanimetaxas.realitycheck.Reality.checkThatCsv;
import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvAssertTest {

  @Test
  public void assertThatCsv_WhenCsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(checkThatCsv(csv));
  }

  @Test
  public void assertThatCsv_CsvIsNull() throws Exception {
    String csv = null;

    assertNotNull(checkThatCsv(csv).isNull());
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_CsvIsNull_AssertNotNull() throws Exception {
    String csv = null;

    checkThatCsv(csv).isNotNull();
  }

  @Test
  public void assertThatCsv_hasSameContentAs() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    assertNotNull(checkThatCsv(csv).hasSameContentAs(csv));
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_hasNotSameContentAs() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    checkThatCsv(csv).hasNotSameContentAs(csv);
  }

  @Test(expected = AssertionError.class)
  public void assertThatCsv_WhenHeaderNotExists() throws Exception {
    String csv = "";

    checkThatCsv(csv).headerHasNoDigits();
  }


}