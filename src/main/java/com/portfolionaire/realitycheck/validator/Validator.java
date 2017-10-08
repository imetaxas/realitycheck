package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 07-Oct-17.
 */
@FunctionalInterface
public interface Validator<T> {

  boolean validate(T value) throws ValidationException;
}
