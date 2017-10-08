package com.portfolionaire.realitycheck.matcher;

import static com.portfolionaire.realitycheck.asserter.FileAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.util.Files;
import java.io.File;
import org.junit.Test;

/**
 * Created by imeta on 07-Oct-17.
 */
public class FileMatcherTest {

  @Test
  public void isCsv() throws Exception {
    File file = Files.toFile("sampleA.csv");

    assertNotNull(assertThat(file).isCsv());
  }

  @Test(expected = AssertionError.class)
  public void isCsv_False() throws Exception {
    File file = Files.toFile("test.txt");

    assertThat(file).isCsv();
  }

  @Test(expected = AssertionError.class)
  public void isCsv_Empty() throws Exception {
    File file = Files.toFile("empty.csv");

    assertThat(file).isCsv();
  }
}