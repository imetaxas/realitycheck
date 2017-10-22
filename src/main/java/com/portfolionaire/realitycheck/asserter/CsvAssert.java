package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.Assertable.asserts;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.matcher.CsvFileMatcher;
import com.portfolionaire.realitycheck.matcher.CsvStringMatcher;
import com.sun.istack.internal.Nullable;
import java.io.File;

/**
 * @author yanimetaxas
 */
public final class CsvAssert extends AbstractAssert {

  CsvAssert(Object csv) {
    super(csv, CsvAssert.class);
  }

  public static CsvFileMatcher assertThatFileCsv(@Nullable File file) {
    return (CsvFileMatcher) asserts(new CsvFileMatcher(file));
  }

  public static CsvFileMatcher assertThatFileCsv(@Nullable String filename)
      throws ValidationException {
    try {
      return assertThatFileCsv(new File(filename));
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }

  public static CsvStringMatcher assertThatCsv(@Nullable String csv) {
    return (CsvStringMatcher) asserts(new CsvStringMatcher(csv));
  }
}
