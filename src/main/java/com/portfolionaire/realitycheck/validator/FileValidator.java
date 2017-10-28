package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * Created by imeta on 08-Oct-17.
 */
public class FileValidator<T, K> extends AbstractValidator<File, File> {

  @Deprecated
  public FileValidator() {
  }

  public FileValidator(File value) {
    super(value);
  }

  @Override
  public File validate(File file) throws ValidationException {
    super.validate(file);
    File resource = IoUtil.loadResource(file.getName());
    if (!resource.exists() || !resource.isFile()) {
      throw new ValidationException("File not found");
    }
    return file;
  }

  @Override
  public File doAction() throws ValidationException {
    return validate(value);
  }
}
