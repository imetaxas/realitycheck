package com.portfolionaire.truecsv.validator;

import com.portfolionaire.truecsv.exception.ValidationException;
import com.portfolionaire.truecsv.util.Files;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * Created by imeta on 08-Oct-17.
 */
public class CsvFileValidator<T> implements Validator<String> {


  @Override
  public boolean validate(String filename) throws ValidationException {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);

    // Validate first
    try (BufferedReader br = new BufferedReader(new FileReader(url.getFile()))) {
      String line;
      boolean isRead = false;
      while ((line = br.readLine()) != null) {
        isRead = true;
        if (line.split(",").length == 1) {
          throw new ValidationException("File " + filename + " has not CSV format");
        }
      }
      if (!isRead) {
        throw new ValidationException("File " + filename + " is empty");
      }
    } catch (IOException ioe) {
      throw new ValidationException(ioe);
    }
    return true;
  }
}
