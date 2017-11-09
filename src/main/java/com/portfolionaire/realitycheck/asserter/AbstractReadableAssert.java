package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
abstract class AbstractReadableAssert<SELF extends AbstractReadableAssert<SELF, ACTUAL>, ACTUAL> extends AbstractAssert<SELF, ACTUAL> {

  private final byte[] actualContent;
  private final ValidationStrategy validationStrategy;

  public AbstractReadableAssert(ACTUAL actual, ValidationStrategy validationStrategy) throws ValidationException {
    super(actual);
    this.validationStrategy = validationStrategy;
    this.actualContent = validateAndRead();
  }

  public byte[] getActualContentOrElse(byte[] value) {
    return Optional.ofNullable(actualContent).orElse(value);
  }

  public byte[] getActualContent() {
    return getActualContentOrElse((byte[])new byte[0]);
  }

  private byte[] validateAndRead() throws ValidationException {
    try {
      return validationStrategy.validate();
    } catch (ValidationException e) {
      return null;
    }
  }
}
