package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;

/**
 * @author yanimetaxas
 */
public class CsvAssert extends InputStreamAssert<CsvAssert, String, CsvValidationStrategy> {

  /*public CsvAssert(String csv) throws ValidationException {
    super(new ByteArrayInputStream(csv.getBytes()));
  }*/
  public CsvAssert(String csv) throws AssertionError {
    super(csv);
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
