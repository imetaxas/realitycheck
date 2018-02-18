package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.CustomObjectValidationStrategy;

/**
 * @author yanimetaxas
 */
public class CustomReadableObjectAssert extends
    AbstractReadableAssert<CustomReadableObjectAssert, CustomObject, CustomObjectValidationStrategy> {

  public CustomReadableObjectAssert(CustomObject customObject) throws ValidationException {
    super(customObject, null);
  }
}
