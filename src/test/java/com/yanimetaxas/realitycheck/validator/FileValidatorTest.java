package com.yanimetaxas.realitycheck.validator;

import static org.junit.Assert.*;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileValidatorTest {

  @Test
  public void validate() throws Exception {
    File file = new File("sampleA.csv");
    FileValidator fileValidator = new FileValidator(file);


    assertNotNull(fileValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsNull() throws Exception {
    FileValidator fileValidator = new FileValidator(null);

    assertNotNull(fileValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileIsEmpty() throws Exception {
    File file = new File("");
    FileValidator fileValidator = new FileValidator(file);

    assertNotNull(fileValidator.validate());
  }

  @Test(expected = ValidationException.class)
  public void validate_FileNotExists() throws Exception {
    File file = new File("");
    FileValidator fileValidator = new FileValidator(file);

    assertNotNull(fileValidator.validate());
  }
}