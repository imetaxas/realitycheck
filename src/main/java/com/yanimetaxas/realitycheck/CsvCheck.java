package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.strategy.CsvValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;

/**
 * @author yanimetaxas
 */
public final class CsvCheck extends
    AbstractReadableCheck<CsvCheck, String, CsvValidationStrategy> {

  CsvCheck(String csv, String message) throws AssertionError {
    super(csv, message);
  }

  CsvCheck hasSameContentAs(String expected) throws AssertionError {
    return (CsvCheck) super.hasSameContentAs(new ByteArrayInputStream(expected.getBytes()));
  }

  public CsvCheck hasNotSameContentAs(String expected) throws AssertionError {
    return (CsvCheck) super.hasNotSameContentAs(new ByteArrayInputStream(expected.getBytes()));
  }

  public CsvCheck headerHasNoDigits() throws AssertionError {
    try {
      String headers = IoUtil.readFirstLine(getActualContent());
      for(String header: headers.split(",")) {
        if (header.matches("[0-9]+")) {
          throw new ValidationException("Header has digits");
        }
      }
    } catch (Exception e) {
      throw new AssertionError(e);
    }
    return this;
  }
}
