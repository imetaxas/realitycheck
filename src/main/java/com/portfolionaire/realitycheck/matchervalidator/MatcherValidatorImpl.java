package com.portfolionaire.realitycheck.matchervalidator;

import com.portfolionaire.realitycheck.validator.Validator;

/**
 * Created by imeta on 08-Oct-17.
 */
public class MatcherValidatorImpl<T> extends AbstractMatcherValidator<T, byte[]> {

  public MatcherValidatorImpl(Validator preValidator, Validator postValidator) {
    super(preValidator, postValidator);
  }
}
