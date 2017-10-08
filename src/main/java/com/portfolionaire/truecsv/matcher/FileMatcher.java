package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.Assert.CsvAssert;
import com.portfolionaire.truecsv.util.Files;
import com.portfolionaire.truecsv.validator.FileValidator;
import com.portfolionaire.truecsv.validator.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by imeta on 24-Sep-17.
 */
public class FileMatcher<T, K> extends AbstractMatcher<String, K> {

  public FileMatcher(String filename) {
    super(filename, new FileValidator());
  }

  public FileMatcher(String filename, Validator<String> validator) {
    super(filename, validator);
  }

  public CsvFileMatcher isCsv() throws Exception {
    return CsvAssert.assertThatFileCsv(actual);
  }

  public FileMatcher isSameAs(File file) throws Exception {
    if(!isEqual(new FileInputStream(Files.toFile(actual)), new FileInputStream(file))){
      throw new Exception("Files are not exactly the same");
    }
    return this;
  }

  public FileMatcher isSameAs(String filename) throws Exception {
    return isSameAs(Files.toFile(filename));
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
    return isNotSameAs(Files.toFile(filename));
  }

  private static boolean isEqual(InputStream i1, InputStream i2) throws IOException {
    ReadableByteChannel ch1 = Channels.newChannel(i1);
    ReadableByteChannel ch2 = Channels.newChannel(i2);
    ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
    ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);
    try {
      while (true) {

        int n1 = ch1.read(buf1);
        int n2 = ch2.read(buf2);

        if (n1 == -1 || n2 == -1) {
          return n1 == n2;
        }

        buf1.flip();
        buf2.flip();

        for (int i = 0; i < Math.min(n1, n2); i++) {
          if (buf1.get() != buf2.get()) {
            return false;
          }
        }

        buf1.compact();
        buf2.compact();
      }

    } finally {
      i1.close();
      i2.close();
      ch1.close();
      ch2.close();
    }
  }
}
