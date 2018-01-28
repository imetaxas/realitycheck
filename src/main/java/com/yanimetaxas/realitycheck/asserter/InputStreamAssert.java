package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.InputStreamValidationStrategy;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class InputStreamAssert extends
    AbstractReadableAssert<InputStreamAssert, InputStream, InputStreamValidationStrategy> {

  public InputStreamAssert(InputStream inputStream) throws AssertionError {
    super(inputStream, null);
  }

  public InputStreamAssert(InputStream inputStream, String message) {
    super(inputStream, message);
  }
}
