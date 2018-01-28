package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import com.yanimetaxas.realitycheck.validator.CsvValidator;
import com.yanimetaxas.realitycheck.validator.FileValidator;
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
    File file = IoUtil.loadResourceOrThrow(filename);

    add(new FileValidator(file));
    add(new CsvValidator(file));

    return super.validate();
  }
}
