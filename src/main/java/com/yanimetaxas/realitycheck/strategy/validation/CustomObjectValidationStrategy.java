package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 09-Nov-17.
 */
public class CustomObjectValidationStrategy extends AbstractValidationStrategy<CustomObject> {

  public CustomObjectValidationStrategy(CustomObject actual) {
    super(actual);
  }


  @Override
  public byte[] validate() throws ValidationException {
    return new byte[0];
  }
}
