package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 10-Dec-17
 */
public class FilepathValidator extends AbstractValidator<String, String> {

  public FilepathValidator(String value) {
    super(value);
  }

  @Override
  public String validate() throws ValidationException {
    super.validate();
    File resource = IoUtil.toFileOrNull(getActualOrThrow(new ValidationException("No value present")));
    if (!resource.exists() || !resource.isFile()) {
      throw new ValidationException("File not found");
    }
    return getActualOrElseNull();
  }

  @Override
  public String doAction() throws ValidationException {
    return validate();
  }
}
