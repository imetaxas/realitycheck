package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class FileValidator extends AbstractValidator<File, File> {

  public FileValidator(File value) {
    super(value);
  }

  @Override
  public File validate() throws ValidationException {
    super.validate();
    File resource = IoUtil.loadFileOrThrow(getActualOrThrow(new ValidationException("No value present")).getName());
    if (!resource.exists() || !resource.isFile()) {
      throw new ValidationException("File not found");
    }
    return getActualOrElseNull();
  }

  @Override
  public File doAction() throws ValidationException {
    return validate();
  }
}
