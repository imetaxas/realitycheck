package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * Created by imeta on 28-Oct-17.
 */
public class FileValidationStrategy extends AbstractValidationStrategy<File, byte[]> {

  public FileValidationStrategy(File actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    File file = new FileValidator(actual.orElseThrow(() -> new ValidationException("No value present"))).doAction();
    return new FileReader(file).doAction();
  }
}
