package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.strategy.validation.FileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.validation.SystemResourceValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public class SystemResourceAssert extends FileAssert {

  public SystemResourceAssert(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message, new SystemResourceValidationStrategy(filename));
  }

  public SystemResourceAssert(File file, String message) throws AssertionError {
    super(file, message, new FileValidationStrategy(file));
  }

  @Override
  public FileAssert hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.loadResource(filename));
  }

  @Override
  public FileAssert hasSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file)));
  }

  @Override
  public FileAssert hasNotSameContentAs(File file) throws AssertionError {
    return (FileAssert) super
        .hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file)));
  }

  @Override
  public FileAssert hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.loadResource(filename));
  }

}
