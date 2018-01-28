package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class FileReader implements Reader<File, byte[]> {

  private File file;

  public FileReader(File file) {
    this.file = file;
  }

  @Override
  public byte[] read() throws ReaderException {
    try {
      File resource = IoUtil.loadResourceOrThrow(file.getName());
      return IOUtils.toByteArray(new java.io.FileReader(resource), "ISO-8859-1");
    } catch (Exception e){
      throw new ReaderException(e);
    }
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return read();
  }
}
