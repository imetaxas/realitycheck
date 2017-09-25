package com.portfolionaire.truecsv.reader;

/**
 * Created by imeta on 24-Sep-17.
 */
public class StringReader<T, K> implements Reader<String, K> {

  @Override
  public K read(String url) throws Exception {
    return (K) new StringBuilder(url).toString();
  }
}
