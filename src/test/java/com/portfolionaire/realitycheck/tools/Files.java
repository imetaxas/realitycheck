package com.portfolionaire.realitycheck.tools;

import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.net.URL;

/**
 * @author yanimetaxas
 */
public class Files {

  public static File loadResource(String filename) throws Exception {
    ClassLoader classLoader = IoUtil.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    try {
      return new File(url.getFile());
    } catch (NullPointerException npe) {
      return new File(filename);
    }
  }
}
