package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.util.IoUtil;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CsvResourceAssertTest {

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = IoUtil.loadResource("withHeader.csv");

    assertNotNull(new CsvResourceAssert(file, null).headerHasNoDigits());
  }
}