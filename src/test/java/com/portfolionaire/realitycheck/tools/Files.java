package com.portfolionaire.realitycheck.tools;

import com.portfolionaire.realitycheck.util.IoUtil;
import java.io.File;
import java.net.URL;

/**
 * Created by imeta on 14-Oct-17.
 */
public class Files {

  @SuppressWarnings("ConstantConditions")
  public static File toFile(String filename) throws Exception {
    ClassLoader classLoader = IoUtil.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return new File(url.getFile());
  }

}
