package com.yanimetaxas.realitycheck.validator;

import static org.junit.Assert.*;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvValidatorTest {

  @Test
  public void validate() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    CsvValidator validator = new CsvValidator(csv);

    assertNotNull(validator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsNull() throws Exception {
    String csv = null;
    CsvValidator validator = new CsvValidator(csv);

    validator.validate();
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsEmpty() throws Exception {
    String csv = "";

    CsvValidator validator = new CsvValidator(csv);

    assertNotNull(validator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsNotCsv() throws Exception {
    String csv = "aaa";

    CsvValidator validator = new CsvValidator(csv);

    assertNotNull(validator.validate());
  }
}