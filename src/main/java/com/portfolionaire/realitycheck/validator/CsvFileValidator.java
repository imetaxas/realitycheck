package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.Reader;

/**
 * Created by imeta on 08-Oct-17.
 */
public class CsvFileValidator implements Validator<String, byte[]> {


  @SuppressWarnings("ConstantConditions")
  @Override
  public byte[] validatedValue(Reader<String, byte[]> reader) throws ValidationException {
    try {
      return reader.read();
    } catch (Exception ioe) {
      throw new ValidationException(ioe);
    }
  }
}
