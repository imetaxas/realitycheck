package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.util.Files;
import java.util.List;

/**
 * Created by imeta on 25-Sep-17.
 */
public class FileReader<T, K> implements Reader<String, List<String>> {

  @Override
  public List<String> read(String filename) throws Exception {
    return Files.readFile(filename);
  }
}
