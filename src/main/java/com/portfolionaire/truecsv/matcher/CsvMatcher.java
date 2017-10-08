package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.validator.CsvValidator;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvMatcher<T, K> extends StringMatcher<T, K> {

  public CsvMatcher(String csv) {
    super(csv, new CsvValidator());
  }
}
