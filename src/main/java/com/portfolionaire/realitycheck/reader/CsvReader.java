package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvReader implements Reader<String, List<String>> {

  private String csv;

  public CsvReader(String csv) {
    this.csv = csv;
  }

  @Override
  public List<String> read() throws ReaderException {
    try {
      return IOUtils.readLines(new StringReader(csv));
    } catch (Exception ioe) {
      throw new ReaderException(ioe);
    }
  }

  @Override
  public List<String> doAction() throws ValidationException {
    return read();
  }
}
