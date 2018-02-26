package com.yanimetaxas.realitycheck.reader;

import static org.junit.Assert.assertNotNull;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import com.yanimetaxas.realitycheck.util.IoUtil;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 11-Feb-18
 */
public class FilepathReaderTest {

  @Test
  public void readFilepath() throws Exception {
    FilepathReader filepathReader = new FilepathReader(IoUtil.loadResource("test.txt").getAbsolutePath());
    byte[] contents = filepathReader.read();

    assertNotNull(contents[0]);
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsNull() throws Exception {
    FilepathReader filepathReader = new FilepathReader(null);

    filepathReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsEmpty() throws Exception {
    FilepathReader filepathReader = new FilepathReader("");

    filepathReader.doAction();
  }

  @Test(expected = ValidationException.class)
  public void readFile_FileIsNotFile() throws Exception {
    FilepathReader filepathReader = new FilepathReader("aaa");

    filepathReader.doAction();
  }

}