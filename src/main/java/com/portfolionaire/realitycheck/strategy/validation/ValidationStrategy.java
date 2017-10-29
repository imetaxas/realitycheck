package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 24-Oct-17.
 */
public interface ValidationStrategy<T, K> {

  K validate() throws ValidationException;
}
