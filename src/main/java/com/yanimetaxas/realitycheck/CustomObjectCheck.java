package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public final class CustomObjectCheck extends AbstractCheck<CustomObjectCheck, CustomObject> {

  public CustomObjectCheck(CustomObject customObject) throws ValidationException {
    super(customObject, null);
  }
}
