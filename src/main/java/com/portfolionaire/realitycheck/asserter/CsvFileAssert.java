package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvFileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.CsvFilenameValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * Created by imeta on 23-Oct-17.
 */
public class CsvFileAssert extends FileAssert {

  private CsvAssert csvAssert;

  public CsvFileAssert(String filename) throws AssertionError {
    super(IoUtil.toFileOrNull(filename), new CsvFilenameValidationStrategy(filename));
  }

  public CsvFileAssert(File csvFile) throws AssertionError {
    super(csvFile, new CsvFileValidationStrategy(csvFile));
    csvAssert = new CsvAssert(new String(getActualContent()));
  }

  public CsvAssert headerHasNoDigits() throws AssertionError {
    return csvAssert.headerHasNoDigits();
  }
  /*public CsvFileMatcher assertThatFileCsv(@Nullable String filename) throws ValidationException {
    try {
      return validate(new File(filename));
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }*/

  /*public CsvStringMatcher assertThatCsv(@Nullable String csv) {
    return (CsvStringMatcher) validate(new CsvStringMatcher(csv));
  }*/

}
