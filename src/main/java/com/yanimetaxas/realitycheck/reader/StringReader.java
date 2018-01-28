package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
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
