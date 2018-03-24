package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public final class Reality {

  private Reality() {
  }

  public static SystemResourceCheck checkThatSystemResource(File file) throws AssertionError {
    return new SystemResourceCheck(file, null);
  }

  public static SystemResourceCheck checkThatSystemResource(String filename) throws AssertionError {
    return new SystemResourceCheck(filename, null);
  }

  public static CsvResourceCheck checkThatCsvResource(String filename) throws AssertionError {
    return new CsvResourceCheck(filename, null);
  }

  public static CsvResourceCheck checkThatCsvResource(File file) throws AssertionError {
    return new CsvResourceCheck(file, null);
  }

  public static FileCheck checkThatFile(String filepath) throws AssertionError {
    return new FileCheck(filepath, null);
  }

  public static CsvFileCheck checkThatCsvFile(String filepath) throws AssertionError {
    return new CsvFileCheck(filepath, null);
  }

  public static CsvFileCheck checkThatCsvFile(File file) throws AssertionError {
    return new CsvFileCheck(file, null);
  }

  public static CsvCheck checkThatCsv(String csv) throws ValidationException {
    return new CsvCheck(csv, null);
  }

  public static FileCheck checkThat(File file) throws AssertionError {
    return new FileCheck(file, null);
  }

  public static IntegerCheck checkThat(Integer integer) throws AssertionError {
    return new IntegerCheck(integer);
  }

  public static BooleanCheck checkThat(Boolean bool) throws AssertionError {
    return new BooleanCheck(bool);
  }

  public static InputStreamCheck checkThat(InputStream inputStream) throws ValidationException {
    return new InputStreamCheck(inputStream);
  }

  public static StringCheck checkThat(String string) throws ValidationException {
    return new StringCheck(string);
  }

  public static CustomObjectCheck checkThat(CustomObject customObject)
      throws ValidationException {
    return new StatementBuilder().that(customObject);
  }

  public static CustomReadableObjectCheck checkThat(CustomReadableObject customReadableObject)
      throws ValidationException {
    return new StatementBuilder().that(customReadableObject);
  }

  public static StatementBuilder checkWithMessage(String message) throws ValidationException {
    return new StatementBuilder().withMessage(message);
  }

}
