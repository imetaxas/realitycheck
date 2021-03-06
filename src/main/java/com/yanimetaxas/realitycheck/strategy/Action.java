package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
@FunctionalInterface
public interface Action<K> {
  K doAction() throws ValidationException;
}
