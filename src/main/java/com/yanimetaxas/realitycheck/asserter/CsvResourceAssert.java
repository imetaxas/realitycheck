package com.yanimetaxas.realitycheck.asserter;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public class CsvResourceAssert extends CsvFileAssert {

  public CsvResourceAssert(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message);
  }

  public CsvResourceAssert(File file, String message) throws AssertionError {
    super(file, message);
  }
}
