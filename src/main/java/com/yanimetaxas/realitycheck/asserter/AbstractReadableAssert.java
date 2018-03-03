package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.validation.ValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author yanimetaxas
 */
public abstract class AbstractReadableAssert<SELF extends AbstractReadableAssert<SELF, ACTUAL, STRATEGY>, ACTUAL, STRATEGY> extends AbstractAssert<SELF, ACTUAL> {

  private final byte[] actualContent;
  private final ValidationStrategy validationStrategy;

  public AbstractReadableAssert(ACTUAL actual, String message) throws AssertionError {
    super(actual, message);
    this.validationStrategy = getValidationStrategyFromType();
    this.actualContent = validateAndRead();
  }

  public AbstractReadableAssert(ACTUAL actual, String message, ValidationStrategy strategy) throws AssertionError {
    super(actual, message);
    this.validationStrategy = strategy;
    this.actualContent = validateAndRead();
  }

  public AbstractReadableAssert hasSameContentAs(InputStream expected) throws AssertionError {
    if(!IoUtil.contentEquals(new ByteArrayInputStream(getActualContent()), expected)){
      throwAssertionErrorWithCustomMessage("InputStreams are NOT exactly the same");
    }
    return self;
  }

  public AbstractReadableAssert hasNotSameContentAs(InputStream expected) throws AssertionError {
    if(IoUtil.contentEquals(new ByteArrayInputStream(getActualContent()), expected)){
      throwAssertionErrorWithCustomMessage("InputStreams are exactly the same");
    }
    return self;
  }

  private byte[] getActualContentOrElse(byte[] value) {
    return Optional.ofNullable(actualContent).orElse(value);
  }

  byte[] getActualContent() {
    return getActualContentOrElse(new byte[0]);
  }

  private ValidationStrategy getValidationStrategyFromType() throws AssertionError {
    Type superclass;
    try {
      superclass = getClass().getGenericSuperclass();
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

  private byte[] validateAndRead() {
    try {
      return validationStrategy.validate();
    } catch (ValidationException e) {
      return null;
    }
  }
}
