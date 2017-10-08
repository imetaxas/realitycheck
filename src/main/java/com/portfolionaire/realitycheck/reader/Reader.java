package com.portfolionaire.realitycheck.reader;

/**
 * Created by imeta on 25-Sep-17.
 */
public interface Reader<T, K> {

  K read(T url) throws Exception;
}
