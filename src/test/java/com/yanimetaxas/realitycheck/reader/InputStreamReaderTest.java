package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.*;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 11-Feb-18
 */
public class InputStreamReaderTest {

  @Test(expected = ValidationException.class)
  public void readFile_FileIsNull() throws Exception {
    InputStreamReader inputStreamReader = new InputStreamReader(null);

    inputStreamReader.doAction();
  }

}