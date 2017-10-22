package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 08-Oct-17.
 */
public class CsvValidator extends AbstractValidator<byte[]> {

  @Override
  public byte[] validate(byte[] csv) throws ValidationException {
    super.validate(csv);
    try {
      List<String> lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv))));

      if (lines.isEmpty()) {
        throw new ValidationException("File is empty");
      }
      for (String line : lines) {
        if (line.split(",").length < 2) {
          throw new ValidationException("String has not CSV format");
        }
      }
    } catch (IOException ioe) {
      throw new ValidationException(ioe);
    }
    return csv;
  }
}
