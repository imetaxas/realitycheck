package com.portfolionaire.truecsv.Assert;

import static com.portfolionaire.truecsv.Assert.Assertable.asserts;

import com.portfolionaire.truecsv.matcher.CsvFileMatcher;
import com.portfolionaire.truecsv.matcher.CsvMatcher;
import java.io.File;
/**
 * Created by imeta on 21-Sep-17.
 */
public class CsvAssert extends AbstractAssert {

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
