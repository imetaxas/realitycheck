package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, ACTUAL_VALUE> implements
    Assertable<SELF, ACTUAL, ACTUAL_VALUE> {

  final Optional<ACTUAL> actual;
  final Optional<ACTUAL_VALUE> actualValue;
  final SELF self;
  private final ValidationStrategy<ACTUAL, ACTUAL_VALUE> validationStrategy;

  public AbstractAssert(ACTUAL actual, Class<?> selfType,
      ValidationStrategy<ACTUAL, ACTUAL_VALUE> validationStrategy) throws ValidationException {
    self = (SELF) selfType.cast(this);
    this.actual = Optional.ofNullable(actual);
    this.validationStrategy = validationStrategy;
    this.actualValue = Optional.ofNullable(validate());
  }

  @Override
  public SELF isNull() {
    if (this.actual != Optional.empty()) {
      throw new AssertionError();
    }
    return self;
  }

  @Override
  public SELF isNotNull() {
    if (this.actual == Optional.empty()) {
      throw new AssertionError();
    }
    return self;
  }

  /*@Override
  public SELF isSameAs(Object expected) throws AssertionError {
    if(!actual.equals(expected)) {
      throw new AssertionError();
    }
    return self;
  }

  @Override
  public SELF isNotSameAs(Object expected) throws AssertionError {
    if(actual.equals(expected)) {
      throw new AssertionError();
    }
    return self;
  }*/

  private ACTUAL_VALUE validate() throws ValidationException {
    try {
      return validationStrategy.validate();
    } catch (ValidationException e) {
      return null;
    }
  }
}
