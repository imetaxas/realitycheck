package com.portfolionaire.realitycheck.reader;

/**
 * Created by imeta on 24-Sep-17.
 */
public class StringReader implements Reader<String, String> {

  private String string;

  public StringReader(String string) {
    this.string = string;
  }

  @Override
  public String read() throws Exception {
    if (string == null) {
      throw new Exception();
    }
    if (string.isEmpty()) {
      throw new Exception();
    }
    return string;
  }

  @Override
  public String getContent() {
    return this.string;
  }
}
