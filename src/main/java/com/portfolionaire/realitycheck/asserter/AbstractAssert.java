package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;

/**
 * @author yanimetaxas
 */
abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, ACTUAL_VALUE> implements
    Assertable<SELF, ACTUAL, ACTUAL_VALUE> {

  final ACTUAL actual;
  final ACTUAL_VALUE actualValue;
  private final SELF self;
  private final ValidationStrategy<ACTUAL, ACTUAL_VALUE> validationStrategy;

  public AbstractAssert(ACTUAL actual, Class<?> selfType,
      ValidationStrategy<ACTUAL, ACTUAL_VALUE> validationStrategy) throws ValidationException {
    self = (SELF) selfType.cast(this);
    this.actual = actual;
    this.validationStrategy = validationStrategy;
    this.actualValue = validate();
  }

  @Override
  public SELF isNull() {
    if (this.actual != null) {
      throw new AssertionError();
    }
    return self;
  }

  @Override
  public ACTUAL_VALUE validate() throws ValidationException {
    return validationStrategy.validate();
  }
}
