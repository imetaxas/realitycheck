package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.custom.CustomReadableObject;
import java.io.File;
import java.io.InputStream;

/**
 * @author yanimetaxas
 * @since 16-Dec-17
 */
public final class StatementBuilder {

  private String message;

  public final StatementBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public final IntegerCheck that(Integer actual) {
    return new IntegerCheck(actual, message);
  }

  public final ObjectCheck that(Object actual) {
    return new ObjectCheck(actual, message);
  }

  public final BooleanCheck that(Boolean actual) {
    return new BooleanCheck(actual, message);
  }

  public final StringCheck that(String actual) {
    return new StringCheck(actual, message);
  }

  public final InputStreamCheck that(InputStream actual) {
    return new InputStreamCheck(actual, message);
  }

  public final FileCheck that(File actual) {
    return new FileCheck(actual, message);
  }

  public final CustomReadableObjectCheck that(CustomReadableObject actual) {
    return actual.getAssertFromType();
  }

  public final CustomObjectCheck that(CustomObject actual) {
    return actual.getAssertFromType();
  }

  public final CsvCheck thatCsv(String actual) {
    return new CsvCheck(actual, message);
  }

  public final CsvFileCheck thatCsvFile(File actual) {
    return new CsvFileCheck(actual, message);
  }

  public final CsvResourceCheck thatCsvResource(File actual) {
    return new CsvResourceCheck(actual, message);
  }

  public final SystemResourceCheck thatSystemResource(File actual) {
    return new SystemResourceCheck(actual, message);
  }
}
