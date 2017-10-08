package com.portfolionaire.truecsv.Assert;

import static com.portfolionaire.truecsv.Assert.Assertable.asserts;

import com.portfolionaire.truecsv.matcher.FileMatcher;
import java.io.File;

/**
 * Created by imeta on 07-Oct-17.
 */
public class FileAssert extends AbstractAssert {

  public static FileMatcher assertThat(File file) {
    return (FileMatcher) asserts(new FileMatcher<>(file.getName()));
  }
}
