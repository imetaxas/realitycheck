package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.strategy.validation.FileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.FilepathValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author yanimetaxas
 */
public class FileAssert extends AbstractReadableAssert<FileAssert, File, FileValidationStrategy> {


  public FileAssert(String filepath) throws FileNotFoundException, AssertionError {
    super(IoUtil.toFileOrNull(filepath), new FilepathValidationStrategy(filepath));
  }

  public FileAssert(File file) throws FileNotFoundException, AssertionError {
    super(file);
  }

  public FileAssert(File file, ValidationStrategy strategy) throws AssertionError {
    super(file, strategy);
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
