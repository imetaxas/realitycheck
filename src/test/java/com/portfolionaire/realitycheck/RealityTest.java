package com.portfolionaire.realitycheck;

import static org.junit.Assert.*;

import com.portfolionaire.realitycheck.Reality;
import com.portfolionaire.realitycheck.asserter.FileAssert;
import com.portfolionaire.realitycheck.tools.CoverageTool;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * Created by imeta on 22-Oct-17.
 */
public class RealityTest {

  @Test(expected = AssertionError.class)
  public void assertThat_FileIsNotNull() throws Exception {
    Reality.assertThat(new File("sampleA.csv")).isNull();
  }

  @Test
  public void assertThat_FileIsNull() throws Exception {
    File file = null;
    assertNotNull(Reality.assertThat(file).isNull());
  }

  @Test
  public void assertThat_FileIsSameAs() throws Exception {
    File file = new File("sampleA.csv");
    assertNotNull(Reality.assertThat(file).isSameAs(file));
  }

  @Test
  public void assertThat_FileIsNotSameAs() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");
    assertNotNull(Reality.assertThat(file1).isNotSameAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void assertThat_FileIsSameAsAndNotSameAs_False() throws Exception {
    File file = new File("sampleA.csv");
    Reality.assertThat(file).isSameAs(file).isNotSameAs(file);
  }

  @Test
  public void assertThat_FileIsSameAsAndNotSameAs_True() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");
    assertNotNull(Reality.assertThat(file1).isSameAs(file1).isNotSameAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void assertThat_FileMultipleIsSame_isFalse() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");

    Reality.assertThat(file1).isSameAs(file1).isSameAs(file2);
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(Reality.class);
  }
}