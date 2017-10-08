package com.portfolionaire.realitycheck.validator;

import com.portfolionaire.realitycheck.util.Files;
import java.io.File;
import java.net.URL;

/**
 * Created by imeta on 08-Oct-17.
 */
public class FileValidator<T> implements Validator<String> {


  @Override
  public boolean validate(String filename)  {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);

    return new File(url.getFile()).exists();
  }
}
