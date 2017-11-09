package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.CustomObject;

/**
 * Created by imeta on 09-Nov-17.
 */
public class CustomObjectAssert extends AbstractReadableAssert<CustomObjectAssert, CustomObject> {


  public CustomObjectAssert(CustomObject customObject, ValidationStrategy validationStrategy) throws ValidationException {
    super(customObject, validationStrategy);
  }
}
