package com.portfolionaire.realitycheck;

import static com.portfolionaire.realitycheck.Reality.assertThat;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.CoverageTool;
import java.io.ByteArrayInputStream;
import java.io.File;
import org.junit.After;
import org.junit.Test;

/**
 * @author yanimetaxas
 */
public class RealityTest {

  @Test(expected = AssertionError.class)
  public void assertThat_FileIsNotNull() throws Exception {
    assertThat(new File("sampleA.csv")).isNull();
  }

  @Test
  public void assertThat_FileIsNull() throws Exception {
    File file = null;
    assertNotNull(assertThat(file).isNull());
  }

  @Test
  public void assertThat_FileIsSameAs() throws Exception {
    File file = new File("sampleA.csv");
    assertNotNull(assertThat(file).hasSameContentAs(file));
  }

  @Test
  public void assertThat_FileIsNotSameAs() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");
    assertNotNull(assertThat(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void assertThat_FileIsSameAsAndNotSameAs_False() throws Exception {
    File file = new File("sampleA.csv");
    assertThat(file).hasSameContentAs(file).hasNotSameContentAs(file);
  }

  @Test
  public void assertThat_FileIsSameAsAndNotSameAs_True() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");
    assertNotNull(assertThat(file1).hasSameContentAs(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void assertThat_FileMultipleIsSame_isFalse() throws Exception {
    File file1 = new File("sampleA.csv");
    File file2 = new File("sampleB.csv");

    assertThat(file1).hasSameContentAs(file1).hasSameContentAs(file2);
  }

  @Test
  public void assertThat_InputStream_IsNotNull() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertNotNull(assertThat(new ByteArrayInputStream(bytes)).isNotNull());
  }

  @Test
  public void assertThat_InputStream_IsNull() throws Exception {
    ByteArrayInputStream inputStream = null;
    assertNotNull(assertThat(inputStream).isNull());
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(Reality.class);
  }
}