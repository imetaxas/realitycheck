package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.matcher.Matchable;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Assertable {

  static Matchable asserts(Matchable matchable) {
    try {
      return matchable.match();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
