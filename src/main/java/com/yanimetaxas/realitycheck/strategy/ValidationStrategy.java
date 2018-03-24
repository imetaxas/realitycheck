package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
@FunctionalInterface
public interface ValidationStrategy<K> {

  byte[] validate() throws ValidationException;
}
