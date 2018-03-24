package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.ValidationStrategy;
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
public abstract class AbstractReadableCheck<SELF extends AbstractReadableCheck<SELF, ACTUAL, STRATEGY>, ACTUAL, STRATEGY> extends
    AbstractCheck<SELF, ACTUAL> {

  private final byte[] actualContent;
  private final ValidationStrategy validationStrategy;

  AbstractReadableCheck(ACTUAL actual, String message) throws AssertionError {
    super(actual, message);
    this.validationStrategy = getValidationStrategyFromType();
    this.actualContent = validateAndRead();
  }

  AbstractReadableCheck(ACTUAL actual, String message, ValidationStrategy strategy) throws AssertionError {
    super(actual, message);
    this.validationStrategy = strategy;
    this.actualContent = validateAndRead();
  }

  AbstractReadableCheck hasSameContentAs(InputStream expected) throws AssertionError {
    if(!IoUtil.contentEquals(new ByteArrayInputStream(getActualContent()), expected)){
      throwAssertionErrorWithCustomMessage("InputStreams are NOT exactly the same");
    }
    return self;
  }

  AbstractReadableCheck hasNotSameContentAs(InputStream expected) throws AssertionError {
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
