package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.validator.CsvFileValidator;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class CsvFileValidationStrategy extends AbstractValidationStrategy<File> {

  public CsvFileValidationStrategy(File fileCsv) {
    super(fileCsv);
  }

  @Override
  public byte[] validate() throws ValidationException {
    return new CsvFileValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
  }
}
