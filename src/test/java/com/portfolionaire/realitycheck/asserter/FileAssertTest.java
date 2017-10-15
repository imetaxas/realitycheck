package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.FileAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.CoverageTool;
import com.portfolionaire.realitycheck.tools.Files;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * Created by imeta on 14-Oct-17.
 */
public class FileAssertTest {

  @Test
  public void isFile() throws Exception {
    File file = Files.toFile("test.txt");

    assertNotNull(assertThat(file));
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(FileAssert.class);
  }
}