package com.portfolionaire.truecsv.util;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imeta on 21-Sep-17.
 */
public class Files {

  private Files() {
  }

  public static File toFile(String filename) throws Exception {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return new File(url.getFile());
  }

  public static List<String> readFile(String filename) throws Exception {
    ClassLoader classLoader = Files.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    List<String> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(url.getFile()))) {
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
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
    return parser.parseLine(csv);
  }
}
