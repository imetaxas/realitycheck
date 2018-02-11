package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.io.File;
import java.io.IOException;
import org.junit.Test;


/**
 * @author yanimetaxas
 */
public class FileReaderTest {

  @Test
  public void readFile() throws Exception {
    FileReader fileReader = new FileReader(new File("test.txt"));
    byte[] contents = fileReader.doAction();

    assertNotNull(contents[0]);
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsNull() throws Exception {
    FileReader fileReader = new FileReader(null);

    fileReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsEmpty() throws Exception {
    FileReader fileReader = new FileReader(new File(""));

    fileReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsNotFile() throws Exception {
    FileReader fileReader = new FileReader(new File("aaa"));

    fileReader.doAction();
  }
}