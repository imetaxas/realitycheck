package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.reader.FileReader;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class FilenameValidationStrategy extends AbstractValidationStrategy<String> {

  public FilenameValidationStrategy(String actual) {
    super(actual);
  }

  @Override
  public byte[] validate() {
    String filename = getActualOrThrow(new ValidationException("No value present"));
    File file = IoUtil.toFile(filename);

    //TODO: VALIDATE HERE
    /*add(new FileValidator(file));
    add(new CsvValidator(file));
    return super.validate();*/
    return new FileReader(file).doAction();
  }
}
