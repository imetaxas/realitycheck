package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 24-Oct-17.
 */
@FunctionalInterface
public interface ValidationStrategy<K> {

  byte[] validate() throws ValidationException;
}
