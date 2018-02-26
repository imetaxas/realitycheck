package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceReader implements Reader<byte[]> {

  private File file;

  public SystemResourceReader(File file) {
    this.file = file;
  }

  @Override
  public byte[] read() throws IOException {
    File resource = IoUtil.loadResource(file.getName());
    return IOUtils.toByteArray(new java.io.FileReader(resource), "ISO-8859-1");
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
