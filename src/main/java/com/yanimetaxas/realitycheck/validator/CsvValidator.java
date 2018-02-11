package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.CsvReader;
import com.yanimetaxas.realitycheck.reader.FileReader;
import java.io.File;
import java.util.List;

/**
 * @author yanimetaxas
 */
public class CsvValidator extends AbstractValidator<String, byte[]> {

  public CsvValidator(String csv) {
    super(csv);
  }

  public CsvValidator(File file) throws ValidationException {
    super(new String(new FileReader(file).read()));
  }

  @Override
  public byte[] validate() throws ValidationException {
    super.validate();
    List lines;
    try {
      lines = new CsvReader(getActualOrElseNull()).doAction();
      if (lines == null || lines.isEmpty()) {
        throw new ValidationException("File is empty");
      }
      for (Object line : lines) {
        if (((String) line).split(",").length < 2) {
          throw new ValidationException("String has not CSV format");
        }
      }
    } catch (Exception e) {
      throw new ValidationException(e);
    }
    return getActualOrElseNull().getBytes();
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return validate();
  }
}
