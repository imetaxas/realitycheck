package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by imeta on 28-Oct-17.
 */
public class InputStreamAssert<SELF, ACTUAL, ACTUAL_VALUE> extends AbstractAssert<InputStreamAssert<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, byte[]> {

  public InputStreamAssert(ACTUAL inputStream, Class<?> selfType, ValidationStrategy<ACTUAL, byte[]> validationStrategy) throws ValidationException {
    super(inputStream, selfType, validationStrategy);
  }


  InputStreamAssert isSameAs(byte[] expected) throws AssertionError {
    try {
      if (!IoUtil.areInputStreamsEqual(new ByteArrayInputStream(actualValue.orElse(new byte[0])), new ByteArrayInputStream(expected))) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception ioe) {
      throw new AssertionError("Expected is not a file", ioe);
    }
    return self;
  }

  InputStreamAssert isNotSameAs(byte[] expected) throws AssertionError {
    try {
      isSameAs(expected);
    } catch (AssertionError ae) {
      return self;
    }
    throw new AssertionError("Rows are exactly the same");
  }
}
