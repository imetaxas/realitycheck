package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;
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

}