package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.CsvFileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.CsvFilenameValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class CsvFileAssert extends FileAssert {

  private CsvAssert csvAssert;

  public CsvFileAssert(String filename, String message) throws AssertionError {
    super(IoUtil.toFileOrNull(filename), message, new CsvFilenameValidationStrategy(filename));
  }

  public CsvFileAssert(File csvFile, String message) throws AssertionError {
    super(csvFile, message, new CsvFileValidationStrategy(csvFile));
    csvAssert = new CsvAssert(new String(getActualContent()), message);
  }

  public CsvAssert headerHasNoDigits() throws AssertionError {
    return csvAssert.headerHasNoDigits();
  }

}
