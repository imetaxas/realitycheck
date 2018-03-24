package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.strategy.FileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.SystemResourceValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public final class SystemResourceCheck extends FileCheck {

  SystemResourceCheck(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message, new SystemResourceValidationStrategy(filename));
  }

  SystemResourceCheck(File file, String message) throws AssertionError {
    super(file, message, new FileValidationStrategy(file));
  }

  @Override
  public FileCheck hasSameContentAs(String filename) throws AssertionError {
    return hasSameContentAs(IoUtil.loadResource(filename));
  }

  @Override
  public FileCheck hasSameContentAs(File file) throws AssertionError {
    return (FileCheck) super
        .hasSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file)));
  }

  @Override
  public FileCheck hasNotSameContentAs(File file) throws AssertionError {
    return (FileCheck) super
        .hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readResource(file)));
  }

  @Override
  public FileCheck hasNotSameContentAs(String filename) throws AssertionError {
    return hasNotSameContentAs(IoUtil.loadResource(filename));
  }

}
