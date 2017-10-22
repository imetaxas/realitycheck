package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class CsvValidator extends AbstractValidator<byte[]> {

  @Override
  public byte[] validate(byte[] csv) throws ValidationException {
    super.validate(csv);
    try {
      List lines = IOUtils
          .readLines(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv))));

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
    return csv;
  }
}
