package com.portfolionaire.truecsv.reader;

import com.portfolionaire.truecsv.util.Files;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvReader<T, K> extends StringReader<T, K> {

  @Override
  public K read(String url) throws Exception {
    return (K) Files.readCsv(url);
  }
}
