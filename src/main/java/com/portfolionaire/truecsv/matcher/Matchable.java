package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.exception.ValidationException;

/**
 * Created by imeta on 24-Sep-17.
 */
@FunctionalInterface
public interface Matchable<T> {

  Matchable<T> validate() throws ValidationException;
}
