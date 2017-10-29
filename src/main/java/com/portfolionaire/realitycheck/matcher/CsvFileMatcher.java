package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.matchervalidator.MatcherValidatorImpl;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class CsvFileMatcher extends FileMatcher<byte[]> {

  public CsvFileMatcher(File file) {
    super(file, new MatcherValidatorImpl<>(new FileValidator(), new CsvValidator()), new FileReader(file));
  }

  private CsvFileMatcher isSameAs(File file) throws AssertionError {
    return (CsvFileMatcher) super.isSameAs(file.getName());
  }

  @Override
  public CsvFileMatcher isSameAs(String filename) throws AssertionError {
    return (CsvFileMatcher) super.isSameAs(filename);
  }

  @Override
  public CsvFileMatcher isNotSameAs(File file) throws AssertionError {
    return (CsvFileMatcher) super.isNotSameAs(file);
  }

  @Override
  public CsvFileMatcher isNotSameAs(String filename) throws AssertionError {
    return (CsvFileMatcher) super.isNotSameAs(filename);
  }

  public CsvFileMatcher headerHasNoDigits() throws AssertionError {
    try {
      String headers = IoUtil.readFirstLine(actualValue);
      for(String header: headers.split(",")) {
        if (header.matches("[0-9]+")) {
          throw new Exception();
        }
      }
    } catch (Exception e) {
      throw new AssertionError();
    }
    return this;
  }
}
