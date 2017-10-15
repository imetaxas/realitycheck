package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 25-Sep-17.
 */
public class FileReader implements Reader<String, byte[]> {

  private String filename;
  private byte[] content;

  public FileReader(String filename) {
    this.filename = filename;
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public byte[] read() throws Exception {
    File file = IoUtil.loadResource(filename);
    return IOUtils.toByteArray(new FileInputStream(file));
  }

  @Override
  public byte[] getContent() {
    return this.content;
  }
}
