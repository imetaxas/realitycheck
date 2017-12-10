package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.Action;

/**
 * @author yanimetaxas
 */
interface Validator<K> extends Action<K> {

  K validate() throws ValidationException;
}
