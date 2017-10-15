package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.reader.CsvFileReader;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.CsvFileValidator;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author yanimetaxas
 */
public class CsvFileMatcher extends FileMatcher<String, List<String[]>> {

  public CsvFileMatcher(String filename) {
    super(new CsvFileValidator(), new CsvFileReader(filename));
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

  public CsvFileMatcher headerHasNoDigits() throws Exception {
    try {
      String[] lineColumns = IoUtil.readFirstLine(actual).split(",");
      for(String column: lineColumns) {
        if (column.matches("[0-9]+")) {
          throw new Exception();
        }
      }
    } catch (Exception e) {
      throw new Exception();
    }

    return this;
  }
}
