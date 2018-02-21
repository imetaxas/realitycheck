package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static com.yanimetaxas.realitycheck.Reality.checkThatCsvFile;
import static com.yanimetaxas.realitycheck.Reality.checkThatCsvResource;
import static com.yanimetaxas.realitycheck.Reality.checkThatFile;
import static com.yanimetaxas.realitycheck.Reality.checkWithMessage;
import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import com.yanimetaxas.realitycheck.tools.CoverageTool;
import com.yanimetaxas.realitycheck.tools.Files;
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
  public void checkFileIsNull_WhenIsNot() throws Exception {
    checkThat(new File("src/test/resources/sampleA.csv")).isNull();
  }

  @Test
  public void checkFileIsNull() throws Exception {
    File file = null;
    assertNotNull(checkThat(file).isNull());
  }

  @Test
  public void checkThatFile_WhenFilepathIsNotNull() throws Exception {
    String filepath = "src/test/resources/sampleA.csv";
    assertNotNull(checkThatFile(filepath).isNotNull());
  }

  @Test
  public void checkThatCsvFile_WhenFilepathIsNotNull() throws Exception {
    String filepath = "src/test/resources/sampleA.csv";
    assertNotNull(checkThatCsvFile(filepath).isNotNull());
  }

  @Test
  public void checkThatCsvFileIsNotNull() throws Exception {
    File file = new File("src/test/resources/sampleA.csv");
    assertNotNull(checkThatCsvFile(file).isNotNull());
  }

  @Test
  public void checkThatCsvIsNotNull() throws Exception {
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";
    assertNotNull(Reality.checkThatCsv(csv).isNotNull());
  }

  @Test
  public void checkFile_WhenHasSameContentAs() throws Exception {
    File file = new File("src/test/resources/sampleA.csv");
    assertNotNull(checkThat(file).hasSameContentAs(file));
  }

  @Test
  public void checkFile_WhenHasNotSameContentAs() throws Exception {
    File file1 = new File("src/test/resources/sampleA.csv");
    File file2 = new File("src/test/resources/sampleB.csv");
    assertNotNull(checkThat(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void checkFile_WhenHasSameContentAs_And_WhenHasNotSameContentAs_False() throws Exception {
    File file = new File("src/test/resources/sampleA.csv");
    checkThat(file).hasSameContentAs(file).hasNotSameContentAs(file);
  }

  @Test
  public void checkFile_WhenHasSameContentAs_And_WhenHasNotSameContentAs_True() throws Exception {
    File file1 = new File("src/test/resources/sampleA.csv");
    File file2 = new File("src/test/resources/sampleB.csv");
    assertNotNull(checkThat(file1).hasSameContentAs(file1).hasNotSameContentAs(file2));
  }

  @Test(expected = AssertionError.class)
  public void checkFile_WhenHasSameContentAs_And_WhenHasSameContentAs_False() throws Exception {
    File file1 = new File("src/test/resources/sampleA.csv");
    File file2 = new File("src/test/resources/sampleB.csv");

    checkThat(file1).hasSameContentAs(file1).hasSameContentAs(file2);
  }

  @Test
  public void checkThatInputStream_IsNotNull() throws Exception {
    byte[] bytes = "RandomString".getBytes();
    assertNotNull(checkThat(new ByteArrayInputStream(bytes)).isNotNull());
  }

  @Test
  public void checkThatInputStream_IsNull() throws Exception {
    ByteArrayInputStream inputStream = null;
    assertNotNull(checkThat(inputStream).isNull());
  }

  @Test
  public void checkWithMessage_WhenStringHasLength() throws Exception {
    String string = "random";
    assertNotNull(checkWithMessage("String has wrong length").that(string).hasLength(6));
  }

  @Test
  public void checkWithMessage_WhenStringHasNotLength() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("String has wrong length");

    String string = "random";
    checkWithMessage("String has wrong length").that(string).hasLength(7);
  }

  @Test
  public void checkWithMessage_WhenBooleanIsTrue() throws Exception {
    assertNotNull(checkWithMessage("Boolean is false").that(true).isTrue());
  }

  @Test
  public void checkWithMessage_WhenBooleanIsNotTrue() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("Boolean is false");

    checkWithMessage("Boolean is false").that(true).isFalse();
  }

  @Test
  public void checkWithMessage_WhenIntegerIsOne() throws Exception {
    assertNotNull(checkWithMessage("Integer is 1").that(1).isEqualTo(1));
  }

  @Test
  public void testEqualsIgnoreCase() throws Exception {
    String string1 = "RandomString";
    String string2 = "RANDOMSTRING";

    assertNotNull(checkThat(string1).equalsIgnoreCase(string2));
  }

  @Test
  public void checkWithMessage_WhenIntegerIsNotOne() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("Integer is wrong");

    checkWithMessage("Integer is wrong").that(1).isNotEqualTo(1);
  }

  @Test
  public void assertThat_assertWithMessage_InputStream_IsNotNull() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("InputStream is NULL");

    byte[] bytes = "RandomString".getBytes();
    checkWithMessage("InputStream is NULL").that(new ByteArrayInputStream(bytes)).isNull();
  }

  @Test
  public void assertThat_assertWithMessage_FileIsNotNull() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("File is NULL");
    File file = null;
    checkWithMessage("File is NULL").that(file).isNotNull();
  }

  @Test
  public void thatCsvFile_assertWithMessage_hasNotSameContentAs_given_file() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("Files have different content");
    File file = Files.loadResource("sampleA.csv");
    checkWithMessage("Files have different content").thatCsvFile(file).hasNotSameContentAs(file.getAbsolutePath());
  }

  @Test
  public void thatCsv_assertWithMessage_hasNotSameContentAs_given_csv() throws Exception {
    expectedEx.expect(AssertionError.class);
    expectedEx.expectMessage("Csv strings have different content");
    String csv = "1,\"Eldon Base for stackable storage shelf, platinum\",Muhammed MacIntyre,3,-213.25,38.94,35,Nunavut,Storage & Organization,0.8";

    checkWithMessage("Csv strings have different content").thatCsv(csv).hasNotSameContentAs(csv);
  }

  @Test
  public void assertThat_CustomObject_IsNotNull() throws Exception {
    assertNotNull(checkThat(new CustomObject("random", 1)).isIntegerGreaterThanZero());
  }

  @Test
  public void checkThatCsvResource_WhenHeaderHasNoDigits() throws Exception {
    File csvResource = Files.loadResource("withHeader.csv");
    assertNotNull(checkThatCsvResource(csvResource).headerHasNoDigits());
  }

  @Test
  public void checkThatCsvResourceNameExists() throws Exception {
    File csvResource = Files.loadResource("withHeader.csv");
    assertNotNull(checkThatCsvResource(csvResource.getName()).exists());
  }

  @Test
  public void checkThatResource() throws Exception {
    File csvResource = Files.loadResource("withHeader.csv");
    assertNotNull(Reality.checkThatResource(csvResource.getName()).exists());
  }

  @Test
  public void checkThatIntegerIsOne() throws Exception {
    assertNotNull(checkThat(1).isEqualTo(1).isNotNull());
  }

  @After
  public void tearDown() throws Exception {
    CoverageTool.testPrivateConstructor(Reality.class);
  }
}