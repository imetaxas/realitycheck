package com.portfolionaire.realitycheck.validator;

import static org.junit.Assert.*;

import com.portfolionaire.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvValidatorTest {

  @Test
  public void validate() throws Exception {
    CsvValidator validator = new CsvValidator();

    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    assertNotNull(validator.validate(csv.getBytes()));
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsNull() throws Exception {
    CsvValidator validator = new CsvValidator();

    validator.validate(null);
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsEmpty() throws Exception {
    CsvValidator validator = new CsvValidator();
    String csv = "";

    assertNotNull(validator.validate(csv.getBytes()));
  }

  @Test(expected = ValidationException.class)
  public void validate_csvIsNotCsv() throws Exception {
    CsvValidator validator = new CsvValidator();
    String csv = "aaa";

    assertNotNull(validator.validate(csv.getBytes()));
  }
}