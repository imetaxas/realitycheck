package com.portfolionaire.truecsv.reader;

import com.portfolionaire.truecsv.util.Files;
import java.util.List;


/**
 * Created by imeta on 24-Sep-17.
 */
public class CsvFileReader extends FileReader<String, List<String[]>> {

  @Override
  public List<String[]> read(String filename) throws Exception {
    return Files.readCsvFile(filename);
  }
}
