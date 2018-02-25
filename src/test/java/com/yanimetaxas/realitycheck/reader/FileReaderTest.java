package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import org.junit.Test;


/**
 * @author yanimetaxas
 */
public class FileReaderTest {

  @Test
  public void readFile() throws Exception {
    FileReader fileReader = new FileReader(new File("src/test/resources/test.txt"));
    byte[] contents = fileReader.doAction();

    assertNotNull(contents[0]);
  }

  @Test(expected = ValidationException.class)
  public void readFile_WhenFileIsNull() throws Exception {
    FileReader fileReader = new FileReader(null);

    fileReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_WhenFileIsEmpty() throws Exception {
    FileReader fileReader = new FileReader(new File(""));

    fileReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_WhenFileIsNotFile() throws Exception {
    FileReader fileReader = new FileReader(new File("aaa"));

    fileReader.doAction();
  }
}