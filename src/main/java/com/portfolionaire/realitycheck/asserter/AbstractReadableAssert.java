package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
abstract class AbstractReadableAssert<SELF extends AbstractReadableAssert<SELF, ACTUAL, STRATEGY>, ACTUAL, STRATEGY> extends AbstractAssert<SELF, ACTUAL> {

  private final byte[] actualContent;
  private final ValidationStrategy validationStrategy;

  public AbstractReadableAssert(ACTUAL actual) throws AssertionError {
    super(actual);
    this.validationStrategy = getValidationStrategyFromType();
    this.actualContent = validateAndRead();
  }

  public AbstractReadableAssert(ACTUAL actual, ValidationStrategy strategy) throws AssertionError {
    super(actual);
    this.validationStrategy = strategy;
    this.actualContent = validateAndRead();
  }

  public byte[] getActualContentOrElse(byte[] value) {
    return Optional.ofNullable(actualContent).orElse(value);
  }

  public byte[] getActualContent() {
    return getActualContentOrElse(new byte[0]);
  }

  private ValidationStrategy getValidationStrategyFromType() throws AssertionError {
    try {
      Type superclass = getClass().getGenericSuperclass();
      Type actualType = ((ParameterizedType) superclass).getActualTypeArguments()[1];
      Class<?> actualRawType = (Class<?>) actualType;
      Type strategyType = ((ParameterizedType) superclass).getActualTypeArguments()[2];
      Class<?> strategyRawType = (Class<?>) strategyType;
      Constructor constructor = strategyRawType.getConstructor(actualRawType);
      return (ValidationStrategy) constructor.newInstance(actual);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  /*private final byte[] actualContent;
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
*/
  private byte[] validateAndRead() {
    try {
      return validationStrategy.validate();
    } catch (ValidationException e) {
      return new byte[0];
    }
  }
}
