package com.portfolionaire.realitycheck.validator;

import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.reader.StringReader;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringValidatorTest {

  @Test
  public void validate() throws Exception {
    StringValidator validator = new StringValidator();

    assertNotNull(validator.validate("string".getBytes()));
  }

  @Test(expected = Exception.class)
  public void validate_isNull() throws Exception {
    StringValidator validator = new StringValidator();
    validator.validate(null);
  }

  @Test(expected = Exception.class)
  public void validate_IsEmpty() throws Exception {
    StringValidator validator = new StringValidator();
    validator.validate("".getBytes());
  }

}