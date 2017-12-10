package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
@FunctionalInterface
public interface Action<K> {
  K doAction() throws ValidationException;
}
