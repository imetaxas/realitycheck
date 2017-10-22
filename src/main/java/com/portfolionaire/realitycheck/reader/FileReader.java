package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ReaderException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
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
      return IOUtils.toByteArray(new FileInputStream(resource));
    } catch (Exception e){
      throw new ReaderException(e);
    }
  }
}
