package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 25-Feb-18
 */
public class SystemResourceReaderTest {

  @Test
  public void readResource() throws Exception {
    SystemResourceReader resourceReader = new SystemResourceReader(
        new File("src/test/resources/test.txt"));
    byte[] contents = resourceReader.doAction();

    assertNotNull(contents[0]);
  }

  @Test(expected = ValidationException.class)
  public void readResource_WhenResourceIsNull() throws Exception {
    SystemResourceReader resourceReader = new SystemResourceReader(null);

    resourceReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_WhenResourceIsEmpty() throws Exception {
    SystemResourceReader resourceReader = new SystemResourceReader(new File(""));

    resourceReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_WhenResourceIsNotFile() throws Exception {
    SystemResourceReader resourceReader = new SystemResourceReader(new File("aaa"));

    resourceReader.doAction();
  }

}