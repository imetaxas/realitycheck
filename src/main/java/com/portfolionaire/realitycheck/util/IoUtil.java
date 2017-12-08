package com.portfolionaire.realitycheck.util;

import com.portfolionaire.realitycheck.exception.ReaderException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Created by imeta on 21-Sep-17.
 */
public class IoUtil {

  private IoUtil() {
  }

  /*public static Optional<File> toFile(String filename)  {
    try {
      ClassLoader classLoader = IoUtil.class.getClassLoader();
      URL url = classLoader.getResource(filename);
      return Optional.ofNullable(new File(url.getFile()));
    } catch (NullPointerException e) {
      return Optional.empty();
    }
  }*/

  public static File toFileOrNull(String filename) {
    try {
      ClassLoader classLoader = IoUtil.class.getClassLoader();
      URL url = classLoader.getResource(filename);
      return new File(url.getFile());
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static File loadFileOrThrow(String filename) {
    try {
      ClassLoader classLoader = IoUtil.class.getClassLoader();
      URL url = classLoader.getResource(filename);
      return new File(url.getFile());
    } catch (NullPointerException e) {
      throw new ReaderException(e);
    }
  }

  public static String readFirstLine(byte[] bytes) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(bytes)));
    return bufferedReader.readLine();
  }

  public static byte[] readFile(String filename) throws AssertionError {
    try {
      File resource = loadFileOrThrow(filename);
      return IOUtils.toByteArray(new java.io.FileReader(resource), "ISO-8859-1");
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  /*public static boolean areInputStreamsEqual(InputStream i1, InputStream i2) throws IOException {
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
  }*/
}