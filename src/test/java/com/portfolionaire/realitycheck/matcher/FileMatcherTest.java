package com.portfolionaire.realitycheck.matcher;

//import static com.portfolionaire.realitycheck.asserter.FileAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.Files;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class FileMatcherTest {

  /*@Test
  public void isCsv() throws Exception {
    File file = IoUtil.toFile("sampleA.csv");

    assertNotNull(assertThat(file).isCsv());
  }

  @Test(expected = AssertionError.class)
  public void isCsv_False() throws Exception {
    File file = IoUtil.toFile("test.txt");

    assertThat(file).isCsv();
  }

  @Test(expected = AssertionError.class)
  public void isCsv_Empty() throws Exception {
    File file = IoUtil.toFile("empty.csv");

    assertThat(file).isCsv();
  }*/
}