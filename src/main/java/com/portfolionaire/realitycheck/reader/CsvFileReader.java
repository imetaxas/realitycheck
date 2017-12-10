package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * @author yanimetaxas
 */
public class CsvFileReader implements Reader<String, List<String>> {

  private String filename;

  CsvFileReader(String filename) {
    this.filename = filename;
  }

  @Override
  public List<String> read() throws ReaderException {
    try {
      File file = IoUtil.loadResourceOrThrow(filename);
      return IOUtils.readLines(new FileInputStream(file));
    } catch (Exception e) {
      throw new ReaderException(e);
    }
  }

  @Override
  public List<String> doAction() throws ValidationException {
    return read();
  }
}
