package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by imeta on 14-Oct-17.
 */
public class StringReaderTest {

  @Test
  public void read() throws Exception {
    StringReader stringReader = new StringReader("string");

    assertNotNull(stringReader.read());
  }

  @Test(expected = Exception.class)
  public void read_isNull() throws Exception {
    StringReader stringReader = new StringReader(null);
    stringReader.read();
  }

  @Test(expected = Exception.class)
  public void read_IsEmpty() throws Exception {
    StringReader stringReader = new StringReader("");
    stringReader.read();
  }
}