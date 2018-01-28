package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.CustomObject;

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
