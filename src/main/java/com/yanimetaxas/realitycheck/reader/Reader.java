package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.strategy.validation.Action;

/**
 * Created by imeta on 25-Sep-17.
 */
public interface Reader<T, K> extends Action<K> {

  K read() throws ReaderException;
}
