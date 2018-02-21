package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.CsvFilenameValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.ValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public class SystemResourceAssert extends FileAssert {

  public SystemResourceAssert(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message, new CsvFilenameValidationStrategy(filename));
  }

  public SystemResourceAssert(File file, String message) throws AssertionError {
    super(file, message);
  }

  public SystemResourceAssert(File file, String message, ValidationStrategy strategy)
      throws AssertionError {
    super(file, message, strategy);
  }

  @Override
  public FileAssert hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.loadResource(filename));
  }

  @Override
  public FileAssert hasSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file.getName())));
  }

  @Override
  public FileAssert hasNotSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file.getName())));
  }

  @Override
  public FileAssert hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.loadResource(filename));
  }

}
