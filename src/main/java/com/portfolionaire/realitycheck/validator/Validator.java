package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.Action;

/**
 * Created by imeta on 07-Oct-17.
 */
public interface Validator<T, K> extends Action<K> {

  K validate() throws ValidationException;
}
