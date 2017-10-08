package com.portfolionaire.truecsv.validator;

import com.portfolionaire.truecsv.exception.ValidationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by imeta on 08-Oct-17.
 */
public class CsvValidator<T> extends StringValidator<String> {

  @Override
  public boolean validate(String csvString) throws ValidationException {
    if (csvString == null) {
      throw new ValidationException("Input is null");
    } else if (csvString.isEmpty()) {
      throw new ValidationException("Input " + csvString + " is empty");
    }

    try (BufferedReader br = new BufferedReader(new StringReader(csvString))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.split(",").length == 1) {
          throw new ValidationException("Input " + csvString + " has not CSV format");
        }
      }
    } catch (IOException ioe) {
      throw new ValidationException(ioe);
    }
    return true;
  }
}
