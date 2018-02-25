package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.asserter.BooleanAssert;
import com.yanimetaxas.realitycheck.asserter.CsvAssert;
import com.yanimetaxas.realitycheck.asserter.CsvFileAssert;
import com.yanimetaxas.realitycheck.asserter.CsvResourceAssert;
import com.yanimetaxas.realitycheck.asserter.CustomObjectAssert;
import com.yanimetaxas.realitycheck.asserter.CustomReadableObjectAssert;
import com.yanimetaxas.realitycheck.asserter.FileAssert;
import com.yanimetaxas.realitycheck.asserter.InputStreamAssert;
import com.yanimetaxas.realitycheck.asserter.IntegerAssert;
import com.yanimetaxas.realitycheck.asserter.StatementBuilder;
import com.yanimetaxas.realitycheck.asserter.StringAssert;
import com.yanimetaxas.realitycheck.asserter.SystemResourceAssert;
import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class Reality {

  private Reality() {
  }

  public static SystemResourceAssert checkThatSystemResource(File file) throws AssertionError {
    return new SystemResourceAssert(file, null);
  }

  public static SystemResourceAssert checkThatSystemResource(String filename) throws AssertionError {
    return new SystemResourceAssert(filename, null);
  }

  public static CsvResourceAssert checkThatCsvResource(String filename) throws AssertionError {
    return new CsvResourceAssert(filename, null);
  }

  public static CsvResourceAssert checkThatCsvResource(File file) throws AssertionError {
    return new CsvResourceAssert(file, null);
  }

  public static FileAssert checkThatFile(String filepath) throws AssertionError {
    return new FileAssert(filepath, null);
  }

  public static CsvFileAssert checkThatCsvFile(String filepath) throws AssertionError {
    return new CsvFileAssert(filepath, null);
  }

  public static CsvFileAssert checkThatCsvFile(File file) throws AssertionError {
    return new CsvFileAssert(file, null);
  }

  public static CsvAssert checkThatCsv(String csv) throws ValidationException {
    return new CsvAssert(csv, null);
  }

  public static FileAssert checkThat(File file) throws AssertionError {
    return new FileAssert(file, null);
  }

  public static IntegerAssert checkThat(Integer integer) throws AssertionError {
    return new IntegerAssert(integer);
  }

  public static BooleanAssert checkThat(Boolean bool) throws AssertionError {
    return new BooleanAssert(bool);
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

  public static CustomObjectAssert checkThat(CustomObject customObject)
      throws ValidationException {
    return new StatementBuilder().that(customObject);
  }

  public static CustomReadableObjectAssert checkThat(CustomReadableObject customReadableObject)
      throws ValidationException {
    return new StatementBuilder().that(customReadableObject);
  }
}
