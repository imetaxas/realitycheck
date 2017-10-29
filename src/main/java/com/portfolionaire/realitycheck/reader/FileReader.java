package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 25-Sep-17.
 */
public class FileReader implements Reader<String, byte[]> {

  private File file;

  public FileReader(File file) {
    this.file = file;
  }

  @Override
  public byte[] read() throws ReaderException {
    try {
      File resource = IoUtil.loadResource(file.getName());
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
