package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.FileReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

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
    try {
      List lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(getActualOrElseNull().getBytes()))));
      if (lines.isEmpty()) {
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
