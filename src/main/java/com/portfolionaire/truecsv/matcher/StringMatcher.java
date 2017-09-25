package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.reader.StringReader;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class StringMatcher<T, K> extends AbstractMatcher<String, K> implements Matchable<String> {

  public StringMatcher(String str, StringReader<T, K> stringReader) {
    super(str, stringReader);
  }
}
