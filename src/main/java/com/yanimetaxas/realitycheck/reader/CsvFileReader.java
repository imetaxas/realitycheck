package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import com.yanimetaxas.realitycheck.validator.CsvValidator;
import com.yanimetaxas.realitycheck.validator.FileValidator;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * @author yanimetaxas
 */
public class CsvFileReader implements Reader<File, byte[]> {

  private File file;

  public CsvFileReader(File file) {
    this.file = file;
  }

  @Override
  public byte[] read() throws ReaderException {
    try {
      new FileValidator(file).doAction();
      return new CsvValidator(file).doAction();
    } catch (ValidationException e) {
      throw new ReaderException(e);
    }
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return read();
  }
}
