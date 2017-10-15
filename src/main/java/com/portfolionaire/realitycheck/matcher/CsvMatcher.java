package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.validator.CsvValidator;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvMatcher extends StringMatcher {

  public CsvMatcher(String csv) {
    super(csv, new CsvValidator());
  }
}
