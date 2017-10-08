package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.util.Files;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvReader<T, K> implements Reader<String, String[]> {

  @Override
  public String[] read(String url) throws Exception {
    return Files.readCsv(url);
  }
}
