package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.CustomObjectValidationStrategy;
import com.yanimetaxas.realitycheck.util.CustomObject;

/**
 * @author yanimetaxas
 */
public class CustomReadableObjectAssert extends
    AbstractReadableAssert<CustomReadableObjectAssert, CustomObject, CustomObjectValidationStrategy> {

  public CustomReadableObjectAssert(CustomObject customObject) throws ValidationException {
    super(customObject, null);
  }

  public CustomReadableObjectAssert isStringNull() throws ValidationException {
    if (actual.getString() != null) {
      throw new ValidationException("");
    }
    return self;
  }

  public CustomReadableObjectAssert isIntegerGreaterThanZero() throws ValidationException {
    if (actual.getInteger() <= 0) {
      throw new ValidationException("");
    }
    return self;
  }
}
