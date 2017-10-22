package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.Assertable.asserts;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.matcher.CsvFileMatcher;
import com.portfolionaire.realitycheck.matcher.CsvStringMatcher;
import java.io.File;
/**
 * @author yanimetaxas
 */
public class CsvAssert extends AbstractAssert {

  private CsvAssert() {
  }

  public static CsvFileMatcher assertThatFileCsv(File file) {
    return (CsvFileMatcher) asserts(new CsvFileMatcher(file));
  }

  public static CsvFileMatcher assertThatFileCsv(String filename) throws ValidationException {
    try {
      return assertThatFileCsv(new File(filename));
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }

  public static CsvStringMatcher assertThatCsv(String csv) {
    return (CsvStringMatcher) asserts(new CsvStringMatcher(csv));
  }

}
