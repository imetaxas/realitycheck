package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 24-Sep-17.
 */
@FunctionalInterface
public interface Matchable<T> {

  Matchable<T> match() throws ValidationException;
}
