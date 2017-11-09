package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;

/**
 * @author yanimetaxas
 */
public class CsvAssert<SELF, ACTUAL, ACTUAL_VALUE> extends InputStreamAssert<CsvAssert<SELF, ACTUAL, ACTUAL_VALUE>, String, byte[]> {

  public CsvAssert(String csv) throws ValidationException {
    super(csv, new CsvValidationStrategy(csv));
  }

  public CsvAssert headerHasNoDigits() throws AssertionError {
    try {
      String headers = IoUtil.readFirstLine(getActualContent());
      for(String header: headers.split(",")) {
        if (header.matches("[0-9]+")) {
          throw new Exception();
        }
      }
    } catch (Exception e) {
      throw new AssertionError();
    }
    return this;
  }
}
