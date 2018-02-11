package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class CsvReader implements Reader<List> {

  private String csv;

  public CsvReader(String csv) {
    this.csv = csv;
  }

  @Override
  public List read() throws IOException {
    return IOUtils.readLines(new StringReader(csv));
  }

  @Override
  public List doAction() throws ValidationException {
    try {
      return read();
    } catch (Exception ioe) {
      throw new ValidationException(ioe.getMessage());
    }
  }
}
