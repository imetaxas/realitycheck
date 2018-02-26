package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.CustomReadableObjectValidationStrategy;

/**
 * @author yanimetaxas
 */
public class CustomReadableObjectAssert extends
    AbstractReadableAssert<CustomReadableObjectAssert, CustomReadableObject, CustomReadableObjectValidationStrategy> {

  public CustomReadableObjectAssert(CustomReadableObject customReadableObject) throws ValidationException {
    super(customReadableObject, null);
  }
}
