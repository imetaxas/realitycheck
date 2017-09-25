package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.reader.Reader;

/**
 * Created by imeta on 25-Sep-17.
 */
public abstract class AbstractMatcher<T, K> implements Matchable<T> {

  private T actual;

  public K headers;
  public K rows;
  public String errorMessage;
  private Reader<T, K> reader;

  public AbstractMatcher(T actual, Reader<T, K> reader) {
    this.actual = actual;
    this.reader = reader;
  }

  @Override
  public Matchable<T> build() throws Exception {
    rows = (K) getReader().read(actual);
    return this;
  }

  public Reader getReader() {
    return reader;
  }

}
