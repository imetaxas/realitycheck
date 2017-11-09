package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.validator.CsvValidator;

/**
 * Created by imeta on 28-Oct-17.
 */
public class CsvValidationStrategy extends AbstractValidationStrategy<String> {

  public CsvValidationStrategy(String actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    return new CsvValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
  }
}
