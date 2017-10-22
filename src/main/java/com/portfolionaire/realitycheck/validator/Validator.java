package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 07-Oct-17.
 */
public interface Validator<T> {

  T validate(T value) throws ValidationException;
}
