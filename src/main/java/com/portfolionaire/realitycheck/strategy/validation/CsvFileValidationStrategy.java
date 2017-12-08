package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.FileValidator;
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
