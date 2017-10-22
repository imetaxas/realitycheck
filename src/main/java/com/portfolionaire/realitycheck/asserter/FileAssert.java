package com.portfolionaire.realitycheck.asserter;

import static com.portfolionaire.realitycheck.asserter.Assertable.asserts;

import com.portfolionaire.realitycheck.matcher.FileMatcher;
import com.portfolionaire.realitycheck.matchervalidator.MatcherValidatorImpl;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.util.IoUtil;
import com.portfolionaire.realitycheck.validator.FileValidator;
import com.portfolionaire.realitycheck.validator.StringValidator;
import com.sun.istack.internal.Nullable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author yanimetaxas
 */
public final class FileAssert extends AbstractAssert<FileAssert, File> {

  FileAssert(File file) {
    super(file, FileAssert.class);
  }

  @Deprecated
  public static FileMatcher assertThat(@Nullable File file) {
    return (FileMatcher) asserts(new FileMatcher(file, new MatcherValidatorImpl<>(new FileValidator(),
        new StringValidator()), new FileReader(file)));
  }

  public FileAssert isSameAs(File file) throws Exception {
    if(isMatcherNull()) {
      this.matcher = assertThat(file);
    }
    ((FileMatcher)matcher).isSameAs(file.getName());
    return this;
  }

  public FileAssert isSameAs(String filename) throws Exception {
    if(isMatcherNull()) {
      this.matcher = assertThat(IoUtil.toFile(filename));
    }
    ((FileMatcher)matcher).isSameAs(filename);
    return this;
  }

  public FileAssert isNotSameAs(File file) throws Exception {
    if(isMatcherNull()) {
      this.matcher = assertThat(file);
    }
    ((FileMatcher)matcher).isNotSameAs(file);
    return this;
  }
}
