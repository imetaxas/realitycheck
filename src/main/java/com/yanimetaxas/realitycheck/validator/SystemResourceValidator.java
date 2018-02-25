package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceValidator extends AbstractValidator<File, File> {

  public SystemResourceValidator(File value) {
    super(value);
  }

  @Override
  public File validate() throws ValidationException {
    super.validate();
    File file = IoUtil.loadResource(getActualOrThrow(new ValidationException("No value present")).getName());
    if (file == null || !file.isFile()) {
      throw new ValidationException("Resource not found");
    }
    return getActualOrElseNull();
  }

  @Override
  public File doAction() throws ValidationException {
    return validate();
  }
}
