package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.InputStreamValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class InputStreamAssert<SELF, ACTUAL, ACTUAL_VALUE> extends AbstractAssert<InputStreamAssert<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, byte[]> {

  public InputStreamAssert(InputStream inputStream) throws ValidationException {
    super((ACTUAL) inputStream, InputStreamAssert.class, new InputStreamValidationStrategy((InputStream) inputStream));
  }

  public InputStreamAssert(ACTUAL inputStream, Class<?> selfType, ValidationStrategy validationStrategy) throws ValidationException {
    super(inputStream, selfType, validationStrategy);
  }

  InputStreamAssert hasSameContentAs(byte[] expected) throws AssertionError {
    try {
      if (!IOUtils.contentEquals(new ByteArrayInputStream(actualValue.orElse(new byte[0])), new ByteArrayInputStream(expected))) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception ioe) {
      throw new AssertionError("Expected is not readable", ioe);
    }
    return self;
  }

  InputStreamAssert hasNotSameContentAs(byte[] expected) throws AssertionError {
    try {
      hasSameContentAs(expected);
    } catch (AssertionError ae) {
      return self;
    }
    throw new AssertionError("Rows are exactly the same");
  }

  InputStreamAssert hasSameContentAs(InputStream expected) throws AssertionError {
    try {
      if(!IOUtils.contentEquals(new ByteArrayInputStream(actualValue.orElse(new byte[0])), expected)) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception ioe) {
      throw new AssertionError("Expected is not an InputStream", ioe);
    }
    return self;
  }

  InputStreamAssert hasNotSameContentAs(InputStream expected) throws AssertionError {
    try {
      hasSameContentAs(expected);
    } catch (AssertionError ae) {
      return self;
    }
    throw new AssertionError("InputStreams are exactly the same");
  }
}
