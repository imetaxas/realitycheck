package com.portfolionaire.realitycheck.matchervalidator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.validator.Validator;

/**
 * Created by imeta on 21-Oct-17.
 */
public abstract class AbstractMatcherValidator<T, K> implements MatcherValidator<T, K> {

  private Validator preValidator;
  private Validator postValidator;

  public AbstractMatcherValidator(Validator preValidator, Validator postValidator) {
    this.preValidator = preValidator;
    this.postValidator = postValidator;
  }

  @Override
  public void preValidate(T value) throws ValidationException {
    preValidator.validate();
  }

  @Override
  public void postValidate(K value) throws ValidationException {
    postValidator.validate();
  }
}
