package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.strategy.validation.Action;

/**
 * Created by imeta on 25-Sep-17.
 */
public interface Reader<T, K> extends Action<T, K> {

  K read() throws ReaderException;
}
