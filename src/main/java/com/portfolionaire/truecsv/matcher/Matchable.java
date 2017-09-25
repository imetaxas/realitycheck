package com.portfolionaire.truecsv.matcher;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Matchable<T> {

  //void describeMismatch(T actual, String mismatchDescription);

  Matchable<T> build() throws Exception;
}
