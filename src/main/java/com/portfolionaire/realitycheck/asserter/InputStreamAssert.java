package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.strategy.validation.InputStreamValidationStrategy;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class InputStreamAssert extends
    AbstractReadableAssert<InputStreamAssert, InputStream, InputStreamValidationStrategy> {

  public InputStreamAssert(InputStream inputStream) throws AssertionError {
    super(inputStream);
  }
}
