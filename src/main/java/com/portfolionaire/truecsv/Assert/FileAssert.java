package com.portfolionaire.truecsv.Assert;

import com.portfolionaire.truecsv.matcher.FileMatcher;
import com.portfolionaire.truecsv.matcher.Matchable;

/**
 * Created by imeta on 24-Sep-17.
 */
public abstract class FileAssert implements Assertable<FileMatcher> {

  public static Matchable asserts(Matchable matcher)  {
    try {
      return matcher.build();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
