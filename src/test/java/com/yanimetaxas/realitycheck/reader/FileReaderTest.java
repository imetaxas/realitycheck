package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.yanimetaxas.realitycheck.exception.ReaderException;
import java.io.File;
import org.junit.Test;


/**
 * @author yanimetaxas
 */
public class FileReaderTest {

  @Test
  public void readFile() throws Exception {
    FileReader fileReader = new FileReader(new File("test.txt"));
    byte[] contents = fileReader.read();

    assertNotNull(contents[0]);
  }

  @Test(expected = ReaderException.class)
  public void readFile_FileIsNull() throws Exception {
    FileReader fileReader = new FileReader(null);

    fileReader.read();
  }

  @Test(expected = ReaderException.class)
  public void readFile_FileIsEmpty() throws Exception {
    FileReader fileReader = new FileReader(new File(""));

    fileReader.read();
  }

  @Test(expected = ReaderException.class)
  public void readFile_FileIsNotFile() throws Exception {
    FileReader fileReader = new FileReader(new File("aaa"));

    fileReader.read();
  }
}