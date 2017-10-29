package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 29-Oct-17.
 */
public class DefaultValidationStrategy extends AbstractValidationStrategy<byte[], byte[]> {

  public DefaultValidationStrategy(byte[] actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    return actual.orElseThrow(() -> new ValidationException("No value present"));
  }
}
