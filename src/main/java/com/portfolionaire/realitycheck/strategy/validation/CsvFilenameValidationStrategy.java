package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * Created by imeta on 29-Oct-17.
 */
public class CsvFilenameValidationStrategy extends AbstractValidationStrategy<String> {

  public CsvFilenameValidationStrategy(String filename) {
    super(filename);
  }

  @Override
  public byte[] validate() throws ValidationException {
    String filename = getActualOrThrow(new ValidationException("No value present"));
    File file = IoUtil.toFile(filename).orElseThrow(AssertionError::new);
    new FileValidator(file).doAction();
    byte[] csvBytes = new FileReader(file).doAction();
    new CsvValidator(new String(csvBytes)).doAction();

    return csvBytes;
  }
}
