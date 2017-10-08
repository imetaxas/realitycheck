package com.portfolionaire.truecsv.Assert;

import com.portfolionaire.truecsv.matcher.Matchable;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Assertable {


  static Matchable asserts(Matchable machable) {
    try {
      return machable.validate();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
