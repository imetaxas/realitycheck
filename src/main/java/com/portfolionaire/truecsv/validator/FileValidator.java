package com.portfolionaire.truecsv.validator;

import com.portfolionaire.truecsv.util.Files;
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
