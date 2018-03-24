package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.CustomReadableObjectValidationStrategy;

/**
 * @author yanimetaxas
 * @since 26-Feb-18
 */
public class CustomReadableTestObjectAssert extends
    AbstractReadableCheck<CustomReadableTestObjectAssert, CustomReadableTestObject, CustomReadableObjectValidationStrategy> {

  public CustomReadableTestObjectAssert(CustomReadableTestObject customReadableTestObject)
      throws ValidationException {
    super(customReadableTestObject, null);
  }
}
