package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class FileReader implements Reader<byte[]> {

  private File file;

  public FileReader(File file) {
    this.file = file;
  }

  @Override
  public byte[] read() throws IOException {
      File resource = IoUtil.toFile(file.getAbsolutePath());
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
