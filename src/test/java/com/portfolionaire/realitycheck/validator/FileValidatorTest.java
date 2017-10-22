package com.portfolionaire.realitycheck.validator;

import static org.junit.Assert.*;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileValidatorTest {

  @Test
  public void validate() throws Exception {
    FileValidator fileValidator = new FileValidator();
    File file = new File("sampleA.csv");

    assertNotNull(fileValidator.validate(file));
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsNull() throws Exception {
    FileValidator fileValidator = new FileValidator();

    assertNotNull(fileValidator.validate(null));
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsEmpty() throws Exception {
    FileValidator fileValidator = new FileValidator();
    File file = new File("");

    assertNotNull(fileValidator.validate(file));
  }

  @Test(expected = ValidationException.class)
  public void validate_FileNotExists() throws Exception {
    FileValidator fileValidator = new FileValidator();
    File file = new File("");

    assertNotNull(fileValidator.validate(file));
  }
}