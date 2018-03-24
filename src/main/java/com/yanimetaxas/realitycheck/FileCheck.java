package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.strategy.FileValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.FilepathValidationStrategy;
import com.yanimetaxas.realitycheck.strategy.ValidationStrategy;
import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class FileCheck extends AbstractReadableCheck<FileCheck, File, FileValidationStrategy> {

  FileCheck(String filepath, String message) throws AssertionError {
    super(IoUtil.toFile(filepath), message, new FilepathValidationStrategy(filepath));
  }

  FileCheck(File file, String message) throws AssertionError {
    super(file, message);
  }

  FileCheck(File file, String message, ValidationStrategy strategy) throws AssertionError {
    super(file, message, strategy);
  }

  public FileCheck exists() {
    if (!getActual().exists()) {
      throw new AssertionError("File " + actual.getName() + " doesn't exist");
    }
    return self;
  }

  public FileCheck doesNotExist() {
    if (getActual().exists()) {
      throw new AssertionError("File " + actual.getName() + " exists");
    }
    return self;
  }

  public FileCheck isDirectory() {
    if (!getActual().isDirectory()) {
      throw new AssertionError("File " + actual.getName() + " is NOT directory");
    }
    return self;
  }

  public FileCheck isNotDirectory() {
    if (getActual().isDirectory()) {
      throw new AssertionError("File " + actual.getName() + " is directory");
    }
    return self;
  }

  public FileCheck isHidden() {
    if (!getActual().isHidden()) {
      throw new AssertionError("File " + actual.getName() + " is NOT hidden");
    }
    return self;
  }

  public FileCheck isNotHidden() {
    if (getActual().isHidden()) {
      throw new AssertionError("File " + actual.getName() + " is hidden");
    }
    return self;
  }

  public FileCheck hasSameContentAs(String filepath) throws AssertionError {
    return hasSameContentAs(IoUtil.toFile(filepath));
  }

  public FileCheck hasSameContentAs(File file) throws AssertionError {
    return (FileCheck) super
        .hasSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getAbsolutePath())));
  }

  public FileCheck hasNotSameContentAs(File file) throws AssertionError {
    return (FileCheck) super
        .hasNotSameContentAs(new ByteArrayInputStream(IoUtil.readFile(file.getAbsolutePath())));
  }

  public FileCheck hasNotSameContentAs(String filepath) throws AssertionError {
    return hasNotSameContentAs(IoUtil.toFile(filepath));
  }
}
