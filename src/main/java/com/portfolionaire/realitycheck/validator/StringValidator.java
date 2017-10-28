package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * Created by imeta on 08-Oct-17.
 */
public class StringValidator extends AbstractValidator<byte[], byte[]> {

  @Deprecated
  public StringValidator() {

  }

  public StringValidator(byte[] value) {
    super(value);
  }

  @Override
  public byte[] validate(byte[] value) throws ValidationException {
    super.validate(value);
    if (new String(value).isEmpty()) {
      throw new ValidationException("Value is empty");
    }
    return value;
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return validate(value);
  }
}
