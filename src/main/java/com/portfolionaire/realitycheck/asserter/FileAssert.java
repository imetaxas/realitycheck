package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.strategy.validation.FileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author yanimetaxas
 */
public class FileAssert extends AbstractReadableAssert<FileAssert, File, FileValidationStrategy> {

  public FileAssert(File file) throws FileNotFoundException, AssertionError {
    super(file);
  }

  public FileAssert(File file, ValidationStrategy strategy) throws AssertionError {
    super(file, strategy);
  }

  public FileAssert hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.loadFileOrThrow(filename));
  }

  public FileAssert hasSameContentAs(File file) throws AssertionError {
    return (FileAssert) super.hasSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getName())));
  }

  public FileAssert hasNotSameContentAs(File file) throws AssertionError {
    return (FileAssert) super.hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getName())));
  }

  public FileAssert hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.loadFileOrThrow(filename));
  }
}
