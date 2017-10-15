package com.portfolionaire.realitycheck.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.junit.Test;


/**
 * Created by imeta on 14-Oct-17.
 */
public class FileReaderTest {

  @Test
  public void readFile() throws Exception {
    FileReader fileReader = new FileReader("test.txt");
    byte[] contents = fileReader.read();

    assertNotNull(contents[0]);
  }

}