package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class CsvReader implements Reader<String, List> {

  private String csv;

  public CsvReader(String csv) {
    this.csv = csv;
  }

  @Override
  public List read() throws ReaderException {
    try {
      return IOUtils.readLines(new StringReader(csv));
    } catch (Exception ioe) {
      throw new ReaderException(ioe);
    }
  }

  @Override
  public List doAction() throws ValidationException {
    return read();
  }
}
