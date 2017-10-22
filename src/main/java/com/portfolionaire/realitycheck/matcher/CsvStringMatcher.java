package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.matchervalidator.MatcherValidatorImpl;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.StringValidator;

/**
 * Created by imeta on 25-Sep-17.
 */
public class CsvStringMatcher extends StringMatcher {

  public CsvStringMatcher(String csv) {
    super(csv, new MatcherValidatorImpl(new StringValidator(), new CsvValidator()));
  }
}
