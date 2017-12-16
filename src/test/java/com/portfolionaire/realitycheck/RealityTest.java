package com.portfolionaire.realitycheck;

import static com.portfolionaire.realitycheck.Reality.assertThat;
import static com.portfolionaire.realitycheck.Reality.assertWithMessage;
import static org.junit.Assert.assertNotNull;

import com.portfolionaire.realitycheck.tools.CoverageTool;
import java.io.ByteArrayInputStream;
import java.io.File;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author yanimetaxas
 */
public class RealityTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

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

  @Test
  public void assertThat_assertWithMessage_thatString_hasLength_6() throws Exception {
    String string = "random";
    assertNotNull(assertWithMessage("String has wrong length").that(string).hasLength(6));
  }

  @Test
  public void assertThat_assertWithMessage_thatString_hasNotLength_7() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("String has wrong length");

    String string = "random";
    assertWithMessage("String has wrong length").that(string).hasLength(7);
  }

  @Test
  public void assertThat_assertWithMessage_InputStream_IsNotNull() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("InputStream is NULL");

    byte[] bytes = "RandomString".getBytes();
    assertWithMessage("InputStream is NULL").that(new ByteArrayInputStream(bytes)).isNull();
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(Reality.class);
  }
}