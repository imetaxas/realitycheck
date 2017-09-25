package com.portfolionaire.truecsv.reader;

import com.portfolionaire.truecsv.util.Files;

/**
 * Created by imeta on 25-Sep-17.
 */
public class FileReader<T, K> implements Reader<String, K> {

  @Override
  public K read(String filename) throws Exception {
    return (K) Files.readFile(filename);
  }
}
