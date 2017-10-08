package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.validator.CsvFileValidator;
import java.io.File;
import java.util.List;

/**
 * @author yanimetaxas
 */
public class CsvFileMatcher extends FileMatcher<String, List<String[]>> {

  public CsvFileMatcher(String filename) {
    super(filename, new CsvFileValidator<>());
  }

  @Override
  public CsvFileMatcher isSameAs(File file) throws Exception {
    return (CsvFileMatcher) super.isSameAs(file);
  }

  @Override
  public CsvFileMatcher isSameAs(String filename) throws Exception {
    return (CsvFileMatcher) super.isSameAs(filename);
  }

  @Override
  public CsvFileMatcher isNotSameAs(File file) throws Exception {
    return (CsvFileMatcher) super.isNotSameAs(file);
  }

  @Override
  public CsvFileMatcher isNotSameAs(String filename) throws Exception {
    return (CsvFileMatcher) super.isNotSameAs(filename);
  }
}
