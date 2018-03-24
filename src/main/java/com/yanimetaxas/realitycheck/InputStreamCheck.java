package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.strategy.InputStreamValidationStrategy;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public final class InputStreamCheck extends
    AbstractReadableCheck<InputStreamCheck, InputStream, InputStreamValidationStrategy> {

  InputStreamCheck(InputStream inputStream) throws AssertionError {
    super(inputStream, null);
  }

  InputStreamCheck(InputStream inputStream, String message) {
    super(inputStream, message);
  }
}
