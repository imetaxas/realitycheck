package com.portfolionaire.realitycheck.reader;

/**
 * Created by imeta on 24-Sep-17.
 */
public class StringReader<T, K> implements Reader<String, String> {

  @Override
  public String read(String url) throws Exception {
    return url;
  }
}
