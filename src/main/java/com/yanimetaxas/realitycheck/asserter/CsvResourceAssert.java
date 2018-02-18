package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.CsvFileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.CsvFilenameValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public class CsvResourceAssert extends SystemResourceAssert {

  private CsvAssert csvAssert;

  public CsvResourceAssert(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message, new CsvFilenameValidationStrategy(filename));
  }

  public CsvResourceAssert(File csvFile, String message) throws AssertionError {
    super(csvFile, message, new CsvFileValidationStrategy(csvFile));
    csvAssert = new CsvAssert(new String(getActualContent()), message);
  }

  public CsvAssert headerHasNoDigits() throws AssertionError {
    return csvAssert.headerHasNoDigits();
  }
}
