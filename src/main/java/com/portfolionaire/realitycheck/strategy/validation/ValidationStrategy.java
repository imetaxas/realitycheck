package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.validator.Validator;

/**
 * Created by imeta on 24-Oct-17.
 */
public interface ValidationStrategy<T, K> {

  K validate() throws ValidationException;

  public T getActual();

  public K getActualValue();
}
