package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class CustomReadableObjectValidationStrategy extends AbstractValidationStrategy<CustomReadableObject> {

  public CustomReadableObjectValidationStrategy(CustomReadableObject actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    return new byte[0];
  }
}
