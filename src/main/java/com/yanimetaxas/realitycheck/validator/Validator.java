package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.Action;

/**
 * @author yanimetaxas
 */
interface Validator<K> extends Action<K> {

  K validate() throws ValidationException;
}
