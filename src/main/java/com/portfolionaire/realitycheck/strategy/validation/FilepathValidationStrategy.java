package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.FilepathReader;
import com.portfolionaire.realitycheck.validator.FilepathValidator;

/**
 * @author yanimetaxas
 * @since 10-Dec-17
 */
public class FilepathValidationStrategy extends AbstractValidationStrategy<String> {

  public FilepathValidationStrategy(String actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    String filepath = new FilepathValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
    return new FilepathReader(filepath).doAction();
  }
}
