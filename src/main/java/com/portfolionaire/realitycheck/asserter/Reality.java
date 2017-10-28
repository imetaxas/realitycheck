package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.matcher.CsvFileMatcher;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.sun.istack.internal.Nullable;
import java.io.File;

/**
 * Created by imeta on 22-Oct-17.
 */
public class Reality {

  private Reality() {
  }

  /*public static CsvAssert assertThat(Object obj) {
    return new CsvAssert(obj);
  }*/

  /*public static CsvAssert assertThat(String csv) {
    return new CsvAssert(csv);
  }*/
  public static CsvFileAssert assertThatFileCsv(@Nullable String filename) throws ValidationException {
    try {
      return new CsvFileAssert(IoUtil.toFile(filename));
    } catch (NullPointerException npe) {
      throw new ValidationException(npe);
    }
  }

  public static CsvFileAssert assertThatFileCsv(@Nullable File file) throws ValidationException {
    return new CsvFileAssert(file);
  }

  public static FileAssert assertThat(File file) throws ValidationException {
    return new FileAssert(file);
  }

  /*public CsvStringMatcher assertThatCsv(@Nullable String csv) {
    return (CsvStringMatcher) validate(new CsvStringMatcher(csv));
  }*/
}
