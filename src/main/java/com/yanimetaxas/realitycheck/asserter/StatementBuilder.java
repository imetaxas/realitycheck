package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.util.CustomObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author yanimetaxas
 * @since 16-Dec-17
 */
public class StatementBuilder {

  private String message;

  public final StatementBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public final StringAssert that(String actual) {
    return new StringAssert(actual, message);
  }

  public final InputStreamAssert that(InputStream actual) {
    return new InputStreamAssert(actual, message);
  }

  public final FileAssert that(File actual) throws FileNotFoundException {
    return new FileAssert(actual, message);
  }

  public final CsvFileAssert thatCsvFile(File actual) {
    return new CsvFileAssert(actual, message);
  }

  public final CsvResourceAssert thatCsvResource(File actual) {
    return new CsvResourceAssert(actual, message);
  }

  public final SystemResourceAssert thatSystemResource(File actual) {
    return new SystemResourceAssert(actual, message);
  }

  public final CsvAssert thatCsv(String actual) {
    return new CsvAssert(actual, message);
  }

  public final CustomReadableObjectAssert that(CustomObject actual) {
    return actual.getAssertFromType();
  }

}
