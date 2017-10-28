package com.portfolionaire.realitycheck.matcher;

//import static com.portfolionaire.realitycheck.asserter.CsvAssert.assertThatFileCsv;
//import static com.portfolionaire.realitycheck.asserter.FileAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.asserter.Reality;
import com.portfolionaire.realitycheck.tools.Files;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class CsvFileMatcherTest {

  /*@Test
  public void headerHasNoDigits() throws Exception {
    File file = Files.toFile("withHeader.csv");

    //assertNotNull(assertThatFileCsv(file).headerHasNoDigits());
    assertNotNull(Reality.assertThatFileCsv(file).headerHasNoDigits());
  }

  @Test(expected = Exception.class)
  public void headerHasNoDigits_NoHeader() throws Exception {
    File file = Files.toFile("sampleA.csv");

    //assertThatFileCsv(file).headerHasNoDigits();
  }*/
}