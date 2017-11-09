package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 09-Nov-17.
 */
public class InputStreamValidationStrategy<ACTUAL> extends AbstractValidationStrategy<InputStream, byte[]> {

  public InputStreamValidationStrategy(InputStream actual) {
    super(actual);
  }

  @Override
  public byte[] validate() throws ValidationException {
    try {
      return IOUtils.toByteArray(actual.orElse(new ByteArrayInputStream(new byte[0])));
    } catch (IOException ioe) {
      throw new ValidationException(ioe);
    }
  }
}
