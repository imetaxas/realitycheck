package com.yanimetaxas.realitycheck.validator;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceValidatorTest {

  @Test
  public void validate() throws Exception {
    File file = new File("src/test/resources/sampleA.csv");
    SystemResourceValidator resourceValidator = new SystemResourceValidator(file);

    assertNotNull(resourceValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsNull() throws Exception {
    SystemResourceValidator resourceValidator = new SystemResourceValidator(null);

    assertNotNull(resourceValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsEmpty() throws Exception {
    SystemResourceValidator resourceValidator = new SystemResourceValidator(new File(""));

    assertNotNull(resourceValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileNotExists() throws Exception {
    SystemResourceValidator resourceValidator = new SystemResourceValidator(new File(""));

    assertNotNull(resourceValidator.validate());
  }

}