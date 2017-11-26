package com.portfolionaire.realitycheck;

import com.portfolionaire.realitycheck.asserter.CsvAssert;
import com.portfolionaire.realitycheck.asserter.CsvFileAssert;
import com.portfolionaire.realitycheck.asserter.FileAssert;
import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by imeta on 22-Oct-17.
 */
public class Reality {

  private Reality() {
  }

  public static CsvFileAssert assertThatFileCsv(String filename) throws AssertionError {
    return new CsvFileAssert(filename);
  }

  public static CsvFileAssert assertThatFileCsv(File file) throws AssertionError {
    return new CsvFileAssert(file);
  }

  public static FileAssert assertThat(File file) throws AssertionError {
    try {
      return new FileAssert(file);
    } catch (FileNotFoundException e) {
      throw new AssertionError(e);
    }
  }

  public static CsvAssert assertThatCsv(String csv) throws ValidationException {
    return new CsvAssert(csv);
  }

  /*public static InputStreamAssert assertThat(InputStream inputStream) throws ValidationException {
    return new InputStreamAssert(inputStream);
  }*/
}
