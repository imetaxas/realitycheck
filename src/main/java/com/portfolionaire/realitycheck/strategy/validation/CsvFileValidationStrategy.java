package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import com.portfolionaire.realitycheck.reader.FileReader;
import com.portfolionaire.realitycheck.validator.CsvValidator;
import com.portfolionaire.realitycheck.validator.FileValidator;
import java.io.File;

/**
 * Created by imeta on 24-Oct-17.
 */
public class CsvFileValidationStrategy<T, K> extends AbstractValidationStrategy<File> {

  /*private FileValidator fileValidator;
  private FileReader fileReader;
  private CsvValidator csvValidator;*/


  /*public CsvFileValidationStrategy(Action<T, K>... actions) {
    super(actions);
  }*/

  /*public CsvFileValidationStrategy(FileValidator fileValidator, FileReader fileReader, CsvValidator csvValidator) {
    this.fileValidator = fileValidator;
    this.fileReader = fileReader;
    this.csvValidator = csvValidator;
  }*/

  public CsvFileValidationStrategy(File fileCsv) {
    super(fileCsv);
  }

  @Override
  public byte[] validate() throws ValidationException {
    new FileValidator(getActualOrThrow(new ValidationException("No value present"))).doAction();
    byte[] csvBytes = new FileReader(getActualOrElse(null)).doAction();
    new CsvValidator(new String(csvBytes)).doAction();

    return csvBytes;
  }
}
