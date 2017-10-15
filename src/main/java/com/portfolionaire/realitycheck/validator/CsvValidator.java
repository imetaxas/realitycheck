package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 08-Oct-17.
 */
public class CsvValidator extends StringValidator {

  @Override
  public String validatedValue(Reader<String, String> reader) throws ValidationException {
    if (reader.getContent() == null) {
      throw new ValidationException("Input is null");
    } else if (reader.getContent().isEmpty()) {
      throw new ValidationException("Csv string is empty");
    }
    try {
      return reader.read();
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }
}
