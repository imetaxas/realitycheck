package com.portfolionaire.realitycheck.matchervalidator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 21-Oct-17.
 */
public interface MatcherValidator<T, K> {

  void preValidate(T value) throws ValidationException;

  void postValidate(K value) throws ValidationException;
}
