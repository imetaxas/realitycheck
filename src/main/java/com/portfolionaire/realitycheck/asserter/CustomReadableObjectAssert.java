package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CustomObjectValidationStrategy;
import com.portfolionaire.realitycheck.util.CustomObject;

/**
 * @author yanimetaxas
 */
class CustomReadableObjectAssert extends AbstractReadableAssert<CustomReadableObjectAssert, CustomObject, CustomObjectValidationStrategy> {


  CustomReadableObjectAssert(CustomObject customObject) throws ValidationException {
    super(customObject, null);
  }

  CustomReadableObjectAssert isStringNull() throws ValidationException {
    if (actual.getString() != null) {
      throw new ValidationException("");
    }
    return self;
  }

  CustomReadableObjectAssert isIntegerGreaterThanZero() throws ValidationException {
    if (actual.getInteger() <= 0) {
      throw new ValidationException("");
    }
    return self;
  }

}
