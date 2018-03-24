package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.CustomReadableObjectValidationStrategy;

/**
 * @author yanimetaxas
 */
public final class CustomReadableObjectCheck extends
    AbstractReadableCheck<CustomReadableObjectCheck, CustomReadableObject, CustomReadableObjectValidationStrategy> {

  public CustomReadableObjectCheck(CustomReadableObject customReadableObject) throws ValidationException {
    super(customReadableObject, null);
  }
}
