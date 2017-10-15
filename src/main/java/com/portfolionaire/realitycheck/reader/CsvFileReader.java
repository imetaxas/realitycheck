package com.portfolionaire.realitycheck.reader;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * Created by imeta on 24-Sep-17.
 */
public class CsvFileReader implements Reader<String, byte[]> {

  private String filename;
  private byte[] content;

  public CsvFileReader(String filename) {
    this.filename = filename;
  }

  @Override
  public byte[] read() throws Exception {
    File file = IoUtil.loadResource(filename);

    content = IOUtils.toByteArray(new FileInputStream(file));

    List<String> lines = IOUtils.readLines(new FileInputStream(file));
    if (lines.isEmpty()) {
      throw new ValidationException("File is empty");
    }
    for (String line : lines) {
      if (line.split(",").length < 2) {
        throw new ValidationException("String has not CSV format");
      }
    }

    return content;
  }

  @Override
  public byte[] getContent() {
    return this.content;
  }
}
