package com.portfolionaire.truecsv.matcher;

import com.portfolionaire.truecsv.reader.CsvFileReader;
import com.portfolionaire.truecsv.util.Files;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by imeta on 21-Sep-17.
 */
public class CsvFileMatcher extends FileMatcher<String, List<String[]>> {

  public CsvFileMatcher(String csv) {
    super(csv, new CsvFileReader());
  }

  public CsvFileMatcher isSameAs(File file) throws Exception {
    List<String[]> allRows = Files.readCsvFile(file);
    int i = 0;
    for(String[] row : allRows){
      if(!Arrays.toString(row).equals(Arrays.toString(rows.get(i)))){
        throw new Exception("Rows are not exactly the same");
      }
      i++;
    }
    return this;
  }

  public CsvFileMatcher isSameAs(String filename) throws Exception {
    return isSameAs(Files.newFile(filename));
  }

  public CsvFileMatcher isNotSameAs(File file) throws Exception {
    try {
       isSameAs(file);
    }catch (Exception e){
      return this;
    }
    throw new Exception("Rows are exactly the same");
  }

  public CsvFileMatcher isNotSameAs(String filename) throws Exception {
    return isNotSameAs(Files.newFile(filename));
  }
}
