package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;

/**
 * Created by imeta on 25-Sep-17.
 */
@FunctionalInterface
public interface Reader<T, K> {

  K read() throws ReaderException;
}
