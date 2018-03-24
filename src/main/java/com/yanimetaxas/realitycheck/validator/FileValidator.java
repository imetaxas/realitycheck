package com.yanimetaxas.realitycheck.validator;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 */
public final class FileValidator extends AbstractValidator<File, File> {

  public FileValidator(File value) {
    super(value);
  }

  @Override
  public File validate() throws ValidationException {
    super.validate();
    File file = IoUtil.toFile(getActualOrThrow(new ValidationException("No value present")).getAbsolutePath());
    if (file == null || !file.isFile()) {
      throw new ValidationException("File not found");
    }
    return getActualOrElseNull();
  }

  @Override
  public File doAction() throws ValidationException {
    return validate();
  }
}
