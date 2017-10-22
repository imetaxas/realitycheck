package com.portfolionaire.realitycheck.asserter;

import java.io.File;

/**
 * Created by imeta on 22-Oct-17.
 */
public class Reality {

  private Reality() {
  }

  /*public static CsvAssert assertThat(Object obj) {
    return new CsvAssert(obj);
  }*/

  /*public static CsvAssert assertThat(String csv) {
    return new CsvAssert(csv);
  }*/

  public static FileAssert assertThat(File file) {
    return new FileAssert(file);
  }
}
