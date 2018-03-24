package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.FileReader;
import com.yanimetaxas.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class FileValidationStrategy extends AbstractValidationStrategy<File> {

  public FileValidationStrategy(File actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    File file = new FileValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
    return new FileReader(file).doAction();
  }
}
