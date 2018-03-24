package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.FilepathReader;
import com.yanimetaxas.realitycheck.validator.FilepathValidator;

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
