package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;

/**
 * @author yanimetaxas
 */
public class StringValidator extends AbstractValidator<String, byte[]> {

  public StringValidator(String value) {
    super(value);
  }

  @Override
  public byte[] validate() throws ValidationException {
    super.validate();
    if (getActualOrThrow(new ValidationException("Value is empty")).isEmpty()) {
      throw new ValidationException("Value is empty");
    }
    return getActualOrElseNull().getBytes();
  }

  @Override
  public byte[] doAction() throws ValidationException {
    return validate();
  }
}
