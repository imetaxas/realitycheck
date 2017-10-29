package com.portfolionaire.realitycheck.matcher;

import com.portfolionaire.realitycheck.matchervalidator.MatcherValidator;
import com.portfolionaire.realitycheck.reader.Reader;
import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author yanimetaxas
 */
public class FileMatcher<K> extends AbstractMatcher<File, byte[]> {

  public FileMatcher() {}

  public FileMatcher(File file, MatcherValidator<File, byte[]> validator, Reader reader) {
    super(file, validator, reader);
  }

  /*public CsvFileMatcher isCsv() throws Exception {
    return new CsvAssert(actual); //.assertThatFileCsv(actual);
  }*/

  public FileMatcher isSameAs(String filename) throws AssertionError {
    return isSameAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }

  private FileMatcher isSameAs(File file) throws AssertionError {
    try {
      if (!IoUtil
          .areInputStreamsEqual(new ByteArrayInputStream(actualValue), new FileInputStream(file))) {
        throw new AssertionError("Not exactly the same");
      }
    } catch (Exception e){
      throw new AssertionError("Expected is not a file");
    }
    return this;
  }

  public FileMatcher isNotSameAs(File file) throws AssertionError {
    try {
      isSameAs(file);
    }catch (Exception e){
      return this;
    }
    throw new AssertionError("Rows are exactly the same");
  }

  public FileMatcher isNotSameAs(String filename) throws AssertionError {
    return isNotSameAs(IoUtil.toFile(filename).orElseThrow(() -> new AssertionError()));
  }
}
