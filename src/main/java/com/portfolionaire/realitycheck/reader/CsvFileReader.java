package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * Created by imeta on 24-Sep-17.
 */
public class CsvFileReader implements Reader<String, List<String>> {

  private String filename;

  public CsvFileReader(String filename) {
    this.filename = filename;
  }

  @Override
  public List<String> read() throws ReaderException {
    try {
      File file = IoUtil.loadResource(filename);
      return IOUtils.readLines(new FileInputStream(file));
    } catch (Exception e) {
      throw new ReaderException(e);
    }
  }
}
