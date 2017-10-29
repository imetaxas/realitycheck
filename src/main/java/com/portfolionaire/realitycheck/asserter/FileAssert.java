package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.FileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class FileAssert<SELF, ACTUAL, ACTUAL_VALUE> extends InputStreamAssert<FileAssert<SELF, ACTUAL, ACTUAL_VALUE>, File, byte[]> {

  public FileAssert(String filename, Class cls, ValidationStrategy strategy) throws ValidationException {
    super(IoUtil.toFile(filename).orElse(new File("")), cls, strategy);
  }

  public FileAssert(File file) throws ValidationException {
    super(file, FileAssert.class, new FileValidationStrategy(file));
  }

  public FileAssert isSameAs(String filename) throws AssertionError, ValidationException {
    return isSameAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }

  public FileAssert isSameAs(File file) throws AssertionError, ValidationException {
    return (FileAssert) super.isSameAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert isNotSameAs(File file) throws AssertionError, ValidationException {
    return (FileAssert) super.isNotSameAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert isNotSameAs(String filename) throws AssertionError, ValidationException {
    return isNotSameAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }
}
