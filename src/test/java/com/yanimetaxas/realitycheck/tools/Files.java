package com.yanimetaxas.realitycheck.tools;

import java.io.File;
import java.net.URL;

/**
 * @author yanimetaxas
 */
public class Files {

  public static File loadResource(String filename) throws Exception {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    try {
      return new File(url.getFile());
    } catch (NullPointerException npe) {
      return isResourceDirectory(filename);
    }
  }

  private static File isResourceDirectory(String filename) {
    File dir = new File("src/test/resources/" + filename);
    if(dir.isDirectory()){
      return dir;
    }
    return new File(filename);
  }
}
