package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 * @since 10-Dec-17
 */
public class FilepathReader implements Reader<byte[]> {

  private String filepath;

  public FilepathReader(String filepath) {
    this.filepath = filepath;
  }

  @Override
  public byte[] read() throws IOException {
      File resource = IoUtil.toFileOrNull(filepath);
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
