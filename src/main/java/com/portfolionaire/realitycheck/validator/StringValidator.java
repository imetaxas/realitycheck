package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 08-Oct-17.
 */
public class StringValidator implements Validator<String, String> {

  @Override
  public String validatedValue(Reader<String, String> reader) throws ValidationException {
    if (reader.getContent() == null) {
      throw new ValidationException("Value is NULL");
    }
    if (reader.getContent().isEmpty()) {
      throw new ValidationException("Value is empty");
    }
    return reader.getContent();
  }
}
