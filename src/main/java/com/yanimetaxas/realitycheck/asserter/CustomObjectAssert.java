package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CustomObjectAssert extends AbstractAssert<CustomObjectAssert, CustomObject> {

  public CustomObjectAssert(CustomObject customObject) throws ValidationException {
    super(customObject, null);
  }

  public CustomObjectAssert isStringNull() throws ValidationException {
    if (actual.getString() != null) {
      throw new ValidationException("");
    }
    return self;
  }

  public CustomObjectAssert isIntegerGreaterThanZero() throws ValidationException {
    if (actual.getInteger() <= 0) {
      throw new ValidationException("");
    }
    return self;
  }
}
