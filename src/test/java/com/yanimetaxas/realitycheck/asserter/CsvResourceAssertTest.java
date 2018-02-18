package com.yanimetaxas.realitycheck.asserter;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.Reality;
import com.yanimetaxas.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CsvResourceAssertTest {

  @Test
  public void headerHasNoDigits() throws Exception {
    File file = Files.loadResource("withHeader.csv");

    assertNotNull(Reality.checkThatCsvResource(file).headerHasNoDigits());
  }
}