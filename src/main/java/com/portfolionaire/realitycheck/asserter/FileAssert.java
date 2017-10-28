package com.portfolionaire.realitycheck.asserter;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.strategy.validation.FileValidationStrategy;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author yanimetaxas
 */
public final class FileAssert extends AbstractAssert<FileAssert, File, byte[]> {

  public FileAssert(File file) throws ValidationException {
    super(file, FileAssert.class, new FileValidationStrategy(file));
  }

  public FileAssert isSameAs(String filename) throws AssertionError {
    return isSameAs(IoUtil.toFile(filename));
  }

  public FileAssert isSameAs(File file) throws AssertionError {
    try {
      if (!IoUtil.areInputStreamsEqual(new ByteArrayInputStream(actualValue), new ByteArrayInputStream(IoUtil.readFile(file, "ISO-8859-1")))) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception ioe){
      throw new AssertionError("Expected is not a file", ioe);
    }
    return this;
  }

  public FileAssert isNotSameAs(File file) throws AssertionError {
    try {
      isSameAs(file);
    }catch (AssertionError e){
      return this;
    }
    throw new AssertionError("Rows are exactly the same");
  }

  public FileAssert isNotSameAs(String filename) throws AssertionError {
    return isNotSameAs(IoUtil.toFile(filename));
  }
}
