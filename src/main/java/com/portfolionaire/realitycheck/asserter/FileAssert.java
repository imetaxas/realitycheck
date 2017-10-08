package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.Assertable.asserts;

import com.portfolionaire.realitycheck.matcher.FileMatcher;
import java.io.File;

/**
 * @author yanimetaxas
 */
public class FileAssert extends AbstractAssert {

  private FileAssert(){
  }

  public static FileMatcher assertThat(File file) {
    return (FileMatcher) asserts(new FileMatcher<>(file.getName()));
  }
}
