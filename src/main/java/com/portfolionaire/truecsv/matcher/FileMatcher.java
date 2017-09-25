package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.reader.FileReader;

/**
 * Created by imeta on 24-Sep-17.
 */
public abstract class FileMatcher<T, K> extends AbstractMatcher<String, K> {

  public FileMatcher(String filename, FileReader<T, K> fileReader) {
    super(filename, fileReader);
  }
}
