package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.CsvFileReader;
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
    return new CsvFileReader(getActualOrThrow(new ValidationException("No value present"))).doAction();
  }
}
