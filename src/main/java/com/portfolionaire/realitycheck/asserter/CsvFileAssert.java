package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvFileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.CsvFilenameValidationStrategy;
import java.io.File;
import java.util.Optional;

/**
 * Created by imeta on 23-Oct-17.
 */
public class CsvFileAssert<SELF, ACTUAL, ACTUAL_VALUE> extends FileAssert<FileAssert<SELF, ACTUAL, ACTUAL_VALUE>, SELF, byte[]> {

  private CsvAssert csvAssert;

  public CsvFileAssert(String filename) throws ValidationException {
    super(filename, new CsvFilenameValidationStrategy(filename));
  }

  public CsvFileAssert(File csvFile) throws ValidationException {
    super(csvFile.getName(), new CsvFileValidationStrategy(csvFile));
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
