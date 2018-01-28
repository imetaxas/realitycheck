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

  public static FileAssert checkThatFile(String filepath) throws AssertionError {
    return new FileAssert(filepath, null);
  }

  public static CsvFileAssert checkThatFileCsv(String filename) throws AssertionError {
    return new CsvFileAssert(filename, null);
  }

  public static CsvFileAssert checkThatFileCsv(File file) throws AssertionError {
    return new CsvFileAssert(file, null);
  }

  public static FileAssert checkThat(File file) throws AssertionError {
    return new FileAssert(file, null);
  }

  public static CsvAssert checkThatCsv(String csv) throws ValidationException {
    return new CsvAssert(csv, null);
  }

  public static InputStreamAssert checkThat(InputStream inputStream) throws ValidationException {
    return new InputStreamAssert(inputStream);
  }

  public static StringAssert checkThat(String string) throws ValidationException {
    return new StringAssert(string);
  }

  public static StatementBuilder checkWithMessage(String message) throws ValidationException {
    return new StatementBuilder().withMessage(message);
  }
}
