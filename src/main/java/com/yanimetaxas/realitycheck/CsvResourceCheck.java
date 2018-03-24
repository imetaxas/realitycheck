package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;

/**
 * @author yanimetaxas
 * @since 17-Feb-18
 */
public final class CsvResourceCheck extends CsvFileCheck {

  CsvResourceCheck(String filename, String message) throws AssertionError {
    super(IoUtil.loadResource(filename), message);
  }

  CsvResourceCheck(File file, String message) throws AssertionError {
    super(file, message);
  }
}
