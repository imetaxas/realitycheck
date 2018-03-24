package com.yanimetaxas.realitycheck.strategy;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class InputStreamValidationStrategy extends AbstractValidationStrategy<InputStream> {

  public InputStreamValidationStrategy(InputStream actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    return new InputStreamReader(getActualOrElse(new ByteArrayInputStream(new byte[0]))).doAction();
  }
}
