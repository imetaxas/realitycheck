package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 24-Sep-17.
 */
public class StringReader implements Reader<String, byte[]> {

  private String string;

  public StringReader(String string) {
    this.string = string;
  }

  @Override
  public byte[] read() throws ReaderException {
    return string.getBytes();
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return read();
  }
}
