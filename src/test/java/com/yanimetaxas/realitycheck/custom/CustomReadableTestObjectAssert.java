package com.yanimetaxas.realitycheck.custom;

import com.yanimetaxas.realitycheck.asserter.AbstractReadableAssert;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.CustomReadableObjectValidationStrategy;

/**
 * @author yanimetaxas
 * @since 26-Feb-18
 */
public class CustomReadableTestObjectAssert extends
    AbstractReadableAssert<CustomReadableTestObjectAssert, CustomReadableTestObject, CustomReadableObjectValidationStrategy> {

  public CustomReadableTestObjectAssert(CustomReadableTestObject customReadableTestObject)
      throws ValidationException {
    super(customReadableTestObject, null);
  }
}
