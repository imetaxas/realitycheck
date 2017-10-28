package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.matcher.Matchable;

/**
 * Created by imeta on 24-Sep-17.
 */
public interface Assertable<SELF extends Assertable<SELF, ACTUAL, ACTUAL_VALUE>, ACTUAL, ACTUAL_VALUE> {

  SELF isNull();

  ACTUAL_VALUE validate() throws ValidationException;
}
