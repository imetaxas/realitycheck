package com.yanimetaxas.realitycheck.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * @author yanimetaxas
 */
public class IoUtil {

  private IoUtil() {
  }

  public static File loadResourceOrNull(String filename) {
    try {
      ClassLoader classLoader = IoUtil.class.getClassLoader();
      URL url = classLoader.getResource(filename);
      return new File(url.getFile());
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static File loadResourceOrThrow(String filename) {
    try {
      ClassLoader classLoader = IoUtil.class.getClassLoader();
      URL url = classLoader.getResource(filename);
      return new File(url.getFile());
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static File toFileOrNull(String filepath) {
    try {
      return new File(filepath);
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static String readFirstLine(byte[] bytes) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(bytes)));
    return bufferedReader.readLine();
  }

  public static byte[] readFile(String filename) throws AssertionError {
    try {
      File resource = loadResourceOrThrow(filename);
      return IOUtils.toByteArray(new java.io.FileReader(resource), "ISO-8859-1");
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public static boolean contentEquals(InputStream inputStream1, InputStream inputStream2) {
    try {
      return IOUtils.contentEquals(inputStream1, inputStream2);
    } catch (Exception ioe) {
      throw new AssertionError("Expected is not an InputStream", ioe);
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
  }
  testing
  */
}
