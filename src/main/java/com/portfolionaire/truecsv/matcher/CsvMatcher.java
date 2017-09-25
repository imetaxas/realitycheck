package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.reader.CsvReader;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvMatcher extends StringMatcher {

  public CsvMatcher(String csv) {
    super(csv, new CsvReader());
  }
}
