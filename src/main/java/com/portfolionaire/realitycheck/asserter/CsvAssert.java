package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.Assertable.asserts;

import com.portfolionaire.realitycheck.matcher.CsvFileMatcher;
import com.portfolionaire.realitycheck.matcher.CsvMatcher;
import java.io.File;
/**
 * @author yanimetaxas
 */
public class CsvAssert extends AbstractAssert {

  private CsvAssert() {
  }

  public static CsvFileMatcher assertThatFileCsv(File file) {
    return assertThatFileCsv(file.getName());
  }

  public static CsvFileMatcher assertThatFileCsv(String filename) {
    return (CsvFileMatcher) asserts(new CsvFileMatcher(filename));
  }

  public static CsvMatcher assertThatCsv(String csv) {
    return (CsvMatcher) asserts(new CsvMatcher(csv));
  }

}
