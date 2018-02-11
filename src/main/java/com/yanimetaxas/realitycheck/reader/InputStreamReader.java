package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 * @since 11-Feb-18
 */
public class InputStreamReader implements Reader<byte[]> {

  private InputStream inputStream;

  public InputStreamReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public byte[] read() throws IOException {
      return IOUtils.toByteArray(inputStream);
  }

  @Override
  public byte[] doAction() throws ValidationException {
    try {
      return read();
    } catch (Exception ioe) {
      throw new ValidationException(ioe.getMessage());
    }
  }

}
