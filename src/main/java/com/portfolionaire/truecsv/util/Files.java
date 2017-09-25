package com.portfolionaire.truecsv.util;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by imeta on 21-Sep-17.
 */
public class Files {

  private Files(){}

  public static File newFile(String filename) throws Exception {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return new File(url.getFile());
  }

  public static List<String> readFile(String filename) throws Exception {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return java.nio.file.Files.readAllLines(Paths.get(url.getFile()), StandardCharsets.UTF_8);
  }

  public static List<String[]> readCsvFile(String filename) throws IOException {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    try (FileReader fileReader = new FileReader(new File(url.getFile()))) {
      return new CSVReader(fileReader).readAll();
    }
  }

  public static String[] readCsv(String csv) throws IOException {
    CSVParser parser = new CSVParser();
    return parser.parseLine(csv) ;
  }

  public static List<String[]> readCsvFile(File file) throws IOException {
    try (FileReader fileReader = new FileReader(file)) {
      CSVReader reader = new CSVReader(fileReader);
      return reader.readAll();
    }
  }
}
