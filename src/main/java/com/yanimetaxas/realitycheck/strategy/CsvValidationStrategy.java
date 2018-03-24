package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.validator.CsvValidator;
import com.yanimetaxas.realitycheck.validator.StringValidator;

/**
 * @author yanimetaxas
 */
public class CsvValidationStrategy extends AbstractValidationStrategy<String> {

  public CsvValidationStrategy(String actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    String validatedString = new StringValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
    return new CsvValidator(validatedString).doAction();
  }
}
