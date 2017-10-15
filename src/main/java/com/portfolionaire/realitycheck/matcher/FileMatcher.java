package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.reader.Reader;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author yanimetaxas
 */
public class FileMatcher<T, K> extends AbstractMatcher<String, byte[]> {

  public FileMatcher(Validator<String, byte[]> validator, Reader reader) {
    super(validator, reader);
  }

  /*public CsvFileMatcher isCsv() throws Exception {
    return CsvAssert.assertThatFileCsv(actual);
  }*/

  public FileMatcher isSameAs(File file) throws Exception {

    if(!IoUtil.areInputStreamsEqual(new ByteArrayInputStream(actual), new FileInputStream(file))){
      throw new Exception("IoUtil are not exactly the same");
    }
    return this;
  }

  public FileMatcher isSameAs(String filename) throws Exception {
    return isSameAs(IoUtil.toFile(filename));
  }

  public FileMatcher isNotSameAs(File file) throws Exception {
    try {
      isSameAs(file);
    }catch (Exception e){
      return this;
    }
    throw new Exception("Rows are exactly the same");
  }

  public FileMatcher isNotSameAs(String filename) throws Exception {
    return isNotSameAs(IoUtil.toFile(filename));
  }
}
