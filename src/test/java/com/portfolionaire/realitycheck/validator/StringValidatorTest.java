package com.portfolionaire.realitycheck.validator;

import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.reader.StringReader;
import org.junit.Test;

/**
 * Created by imeta on 14-Oct-17.
 */
public class StringValidatorTest {

  @Test
  public void validate() throws Exception {
    StringValidator validator = new StringValidator();

    assertNotNull(validator.validatedValue(new StringReader("string")));
  }

  @Test(expected = Exception.class)
  public void validate_isNull() throws Exception {
    StringValidator validator = new StringValidator();
    validator.validatedValue(new StringReader(null));
  }

  @Test(expected = Exception.class)
  public void validate_IsEmpty() throws Exception {
    StringValidator validator = new StringValidator();
    validator.validatedValue(new StringReader(""));
  }

}