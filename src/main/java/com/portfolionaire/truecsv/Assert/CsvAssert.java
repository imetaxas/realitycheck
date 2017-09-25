package com.portfolionaire.truecsv.Assert;

import com.portfolionaire.truecsv.matcher.CsvFileMatcher;
import com.portfolionaire.truecsv.matcher.CsvMatcher;
import java.io.File;

/**
 * Created by imeta on 21-Sep-17.
 */
public class CsvAssert extends FileAssert {

  public static CsvFileMatcher assertThatFileCsv(File file) {
    return assertThatFileCsv(file.getName());
  }

  public static CsvFileMatcher assertThatFileCsv(String filename) {
    return (CsvFileMatcher) asserts(new CsvFileMatcher(filename));
  }

  public static CsvFileMatcher assertThatCsv(String csv) {
    return (CsvFileMatcher) asserts(new CsvMatcher(csv));
  }

}
