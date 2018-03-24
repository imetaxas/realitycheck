package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;


/**
 * @author yanimetaxas
 */
public final class CsvFileValidator extends AbstractValidator<File, byte[]> {

  public CsvFileValidator(File file) throws ValidationException {
    super(file);
  }

  @Override
  public byte[] validate() throws ValidationException {
      File file = new FileValidator(getActualOrElseNull()).doAction();
      return new CsvValidator(file).doAction();
  }

  @Override
  public byte[] doAction() throws ValidationException {
      return validate();
  }
}
