package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.CsvValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 */
public final class CsvAssert extends AbstractAssert<CsvAssert, String, byte[]> {

  public CsvAssert(String csv) throws ValidationException {
    super(csv, CsvAssert.class, new CsvValidationStrategy(csv));
  }

  public CsvAssert isSameAs(String filename) throws AssertionError {
    return isSameAs(IoUtil.toFile(filename));
  }

  private CsvAssert isSameAs(File file) throws AssertionError {
    try {
      if (!IoUtil.areInputStreamsEqual(new ByteArrayInputStream(actualValue),
          new ByteArrayInputStream(IoUtil.readFile(file, "ISO-8859-1")))) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception ioe) {
      throw new AssertionError("Expected is not a file", ioe);
    }
    return this;
  }

  public CsvAssert isNotSameAs(File file) throws AssertionError {
    try {
      isSameAs(file);
    } catch (AssertionError ae) {
      return this;
    }
    throw new AssertionError("Rows are exactly the same");
  }

  public CsvAssert isNotSameAs(String filename) throws AssertionError {
    return isNotSameAs(IoUtil.toFile(filename));
  }
}
