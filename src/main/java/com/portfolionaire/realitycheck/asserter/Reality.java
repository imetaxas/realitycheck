package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
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

  public static CsvFileAssert assertThatFileCsv(String filename) throws ValidationException {
    return new CsvFileAssert(filename);
  }

  public static CsvFileAssert assertThatFileCsv(File file) throws ValidationException {
    return new CsvFileAssert(file);
  }

  public static FileAssert assertThat(File file) throws ValidationException {
    return new FileAssert(file);
  }

  public static CsvAssert assertThatCsv(String csv) throws ValidationException {
    return new CsvAssert(csv);
  }
}
