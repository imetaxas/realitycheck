package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvFileValidationStrategy;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * Created by imeta on 23-Oct-17.
 */
public class CsvFileAssert extends AbstractAssert<CsvFileAssert, File, byte[]> {

  private CsvAssert csvAssert;
  private FileAssert fileAssert;
  private FileReader fileReader;

  public CsvFileAssert(File csv) throws ValidationException {
    super(csv, CsvFileAssert.class, new CsvFileValidationStrategy<>(csv));

    fileAssert = new FileAssert(csv);
    csvAssert = new CsvAssert(new String(actualValue));
  }

  public CsvFileAssert headerHasNoDigits() throws AssertionError {
    try {
      String headers = IoUtil.readFirstLine(actualValue);
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

  public CsvFileAssert isNotSameAs(File file) {
    csvAssert.isNotSameAs(file);
    return this;
  }

  public CsvFileAssert isNotSameAs(String filename) {
    csvAssert.isNotSameAs(filename);
    return this;
  }

  public CsvFileAssert isSameAs(String filename) {
    csvAssert.isSameAs(filename);
    return this;
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
