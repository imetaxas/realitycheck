package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.FileValidationStrategy;
import com.portfolionaire.realitycheck.strategy.validation.ValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author yanimetaxas
 */
public class FileAssert extends InputStreamAssert<FileAssert, File, FileValidationStrategy> {

  public FileAssert(File file) throws FileNotFoundException, AssertionError {
    super(file);
  }

  public FileAssert(File file, ValidationStrategy strategy) throws AssertionError {
    super(file, strategy);
  }

  public FileAssert hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.toFileOrThrow(filename));
  }

  public FileAssert hasSameContentAs(File file) throws AssertionError {
    return (FileAssert) super.hasSameContentAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert hasNotSameContentAs(File file) throws AssertionError {
    return (FileAssert) super.hasNotSameContentAs(IoUtil.readFile(file.getName()));
  }

  public FileAssert hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.toFileOrThrow(filename));
  }
}
