package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.SystemResourceReader;
import com.yanimetaxas.realitycheck.util.IoUtil;
import com.yanimetaxas.realitycheck.validator.SystemResourceValidator;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceValidationStrategy extends AbstractValidationStrategy<String> {

  public SystemResourceValidationStrategy(String actual) {
    super(actual);
  }

  @Override
  public byte[] validate() {
    String filename = getActualOrThrow(new ValidationException("No value present"));
    File file = IoUtil.loadResource(filename);

    add(new SystemResourceValidator(file));
    add(new SystemResourceReader(file));
    return super.validate();
  }
}
