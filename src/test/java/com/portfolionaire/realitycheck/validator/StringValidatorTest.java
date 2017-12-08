package com.portfolionaire.realitycheck.validator;

import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.StringReader;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringValidatorTest {

  @Test
  public void validate() throws Exception {
    StringValidator validator = new StringValidator("string".getBytes());

    assertNotNull(validator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_isNull() throws Exception {
    StringValidator validator = new StringValidator(null);
    validator.validate();
  }

  @Test(expected = ValidationException.class)
  public void validate_IsEmpty() throws Exception {
    StringValidator validator = new StringValidator("".getBytes());
    validator.validate();
  }

}