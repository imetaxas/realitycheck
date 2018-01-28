package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.FileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.FilepathValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.ValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class FileAssert extends AbstractReadableAssert<FileAssert, File, FileValidationStrategy> {


  public FileAssert(String filepath, String message) throws AssertionError {
    super(IoUtil.toFileOrNull(filepath), message, new FilepathValidationStrategy(filepath));
  }

  public FileAssert(File file, String message) throws AssertionError {
    super(file, message);
  }

  public FileAssert(File file, String message, ValidationStrategy strategy) throws AssertionError {
    super(file, message, strategy);
  }

  public FileAssert exists() {
    if (!getActual().exists()) {
      throw new AssertionError("File " + actual.getName() + " doesn't exist");
    }
    return self;
  }

  public FileAssert notExists() {
    if (getActual().exists()) {
      throw new AssertionError("File " + actual.getName() + " exists");
    }
    return self;
  }

  public FileAssert hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.loadResourceOrThrow(filename));
  }

  public FileAssert hasSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getName())));
  }

  public FileAssert hasNotSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getName())));
  }

  public FileAssert hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.loadResourceOrThrow(filename));
  }
}
