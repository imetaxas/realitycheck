package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class StringReaderTest {

  @Test
  public void read() throws Exception {
    StringReader stringReader = new StringReader("string");

    assertNotNull(stringReader.read());
  }

  @Test
  public void read_IsEmpty() throws Exception {
    StringReader stringReader = new StringReader("");

    assertNotNull(stringReader.read());
  }
}