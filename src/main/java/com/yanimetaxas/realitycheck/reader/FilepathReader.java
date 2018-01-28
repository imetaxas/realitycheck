package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 * @since 10-Dec-17
 */
public class FilepathReader implements Reader<String, byte[]> {

  private String filepath;

  public FilepathReader(String filepath) {
    this.filepath = filepath;
  }

  @Override
  public byte[] read() throws ReaderException {
    try {
      File resource = IoUtil.toFileOrNull(filepath);
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
