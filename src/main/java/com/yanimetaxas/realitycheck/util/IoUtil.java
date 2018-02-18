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

  public static File loadResource(String filename) {
    ClassLoader classLoader = IoUtil.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    try {
      return new File(url.getFile());
    } catch (NullPointerException e) {
      return isResourceDirectory(filename);
    }
  }

  private static File isResourceDirectory(String filename) {
    File dir = new File("src/test/resources/" + filename);
    if(dir.isDirectory()){
      return dir;
    }
    return null;
  }

  public static File toFile(String filepath) {
    try {
      return new File(filepath);
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static byte[] readFile(String filepath) {
    try {
      File file = toFile(filepath);
      return IOUtils.toByteArray(new java.io.FileReader(file), "ISO-8859-1");
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public static String readFirstLine(byte[] bytes) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(bytes)));
    return bufferedReader.readLine();
  }

  public static byte[] readResource(String filename) throws AssertionError {
    try {
      File resource = loadResource(filename);
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
}
