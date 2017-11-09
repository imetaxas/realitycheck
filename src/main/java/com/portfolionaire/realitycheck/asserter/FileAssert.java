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

  public FileAssert hasSameContentAs(String filename) throws AssertionError, ValidationException {
    return hasSameContentAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }

  public FileAssert hasSameContentAs(File file) throws AssertionError, ValidationException {
    return (FileAssert) super.hasSameContentAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert hasNotSameContentAs(File file) throws AssertionError, ValidationException {
    return (FileAssert) super.hasNotSameContentAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert hasNotSameContentAs(String filename) throws AssertionError, ValidationException {
    return hasNotSameContentAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }
}
