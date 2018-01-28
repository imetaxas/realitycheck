package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.asserter.CsvAssert;
import com.yanimetaxas.realitycheck.asserter.CsvFileAssert;
import com.yanimetaxas.realitycheck.asserter.FileAssert;
import com.yanimetaxas.realitycheck.asserter.InputStreamAssert;
import com.yanimetaxas.realitycheck.asserter.StatementBuilder;
import com.yanimetaxas.realitycheck.asserter.StringAssert;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class Reality {

  private Reality() {
  }

  public static FileAssert assertThatFile(String filepath) throws AssertionError {
    return new FileAssert(filepath, null);
  }

  public static CsvFileAssert assertThatFileCsv(String filename) throws AssertionError {
    return new CsvFileAssert(filename, null);
  }

  public static CsvFileAssert assertThatFileCsv(File file) throws AssertionError {
    return new CsvFileAssert(file, null);
  }

  public static FileAssert assertThat(File file) throws AssertionError {
    return new FileAssert(file, null);
  }

  public static CsvAssert assertThatCsv(String csv) throws ValidationException {
    return new CsvAssert(csv, null);
  }

  public static InputStreamAssert assertThat(InputStream inputStream) throws ValidationException {
    return new InputStreamAssert(inputStream);
  }

  public static StringAssert assertThat(String string) throws ValidationException {
    return new StringAssert(string);
  }

  public static StatementBuilder assertWithMessage(String message) throws ValidationException {
    return new StatementBuilder().withMessage(message);
  }
}
