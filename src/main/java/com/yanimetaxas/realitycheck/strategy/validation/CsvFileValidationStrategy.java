package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.FileReader;
import com.yanimetaxas.realitycheck.validator.CsvValidator;
import com.yanimetaxas.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * Created by imeta on 24-Oct-17.
 */
public class CsvFileValidationStrategy extends AbstractValidationStrategy<File> {

  public CsvFileValidationStrategy(File fileCsv) {
    super(fileCsv);
  }

  @Override
  public byte[] validate() throws ValidationException {
    new FileValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
    byte[] csvBytes = new FileReader(getActualOrElse(null)).doAction();
    new CsvValidator(new String(csvBytes)).doAction();

    return csvBytes;
  }
}
