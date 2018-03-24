package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.strategy.CsvFileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.CsvFilenameValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class CsvFileCheck extends FileCheck {

  private CsvCheck csvCheck;

  CsvFileCheck(String filepath, String message) throws AssertionError {
    super(IoUtil.toFile(filepath), message, new CsvFilenameValidationStrategy(filepath));
  }

  CsvFileCheck(File csvFile, String message) throws AssertionError {
    super(csvFile, message, new CsvFileValidationStrategy(csvFile));
    csvCheck = new CsvCheck(new String(getActualContent()), message);
  }

  public CsvCheck headerHasNoDigits() throws AssertionError {
    return csvCheck.headerHasNoDigits();
  }

}
