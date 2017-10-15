package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 07-Oct-17.
 */
@FunctionalInterface
public interface Validator<T, K> {

  K validatedValue(Reader<T, K> reader) throws ValidationException;
}
