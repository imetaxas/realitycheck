package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class CsvFilenameValidationStrategy extends AbstractValidationStrategy<String> {

  public CsvFilenameValidationStrategy(String filename) {
    super(filename);
  }

  @Override
  public byte[] validate() {
    String filename = getActualOrThrow(new ValidationException("No value present"));
    File file = IoUtil.loadFileOrThrow(filename);

    add(new FileValidator(file));
    add(new CsvValidator(file));

    return super.validate();
  }
}
