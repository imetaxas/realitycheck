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
  public byte[] validate() throws ValidationException {
    super.validate();
    if (new String(value.get()).isEmpty()) {
      throw new ValidationException("Value is empty");
    }
    return value.get();
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return validate();
  }
}
