package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
class StringValidator extends AbstractValidator<byte[], byte[]> {

  public StringValidator(byte[] value) {
    super(value);
  }

  @Override
  public byte[] validate() throws ValidationException {
    super.validate();
    if (new String(getActualOrThrow(new ValidationException("Value is empty"))).isEmpty()) {
      throw new ValidationException("Value is empty");
    }
    return getActualOrElseNull();
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return validate();
  }
}
