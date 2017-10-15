package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvReader implements Reader<String, List<String[]>> {

  private String csv;
  private List<String[]> content;

  public CsvReader(String csv) {
    this.csv = csv;
  }

  @Override
  public List<String[]> read() throws Exception {
    content = new ArrayList<>();
    List<String> lines = IOUtils.readLines(new StringReader(csv));
    for (String line : lines) {
      String[] lineRows = line.split(",");
      if (lineRows.length < 2) {
        throw new ValidationException("String has not CSV format");
      }
      content.add(lineRows);
    }
    return content;
  }

  @Override
  public List<String[]> getContent() {
    return this.content;
  }
}
