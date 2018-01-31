[![Main Site](https://img.shields.io/badge/imetaxas.github.io/realitycheck-ff55ff.png?style=flat)](https://imetaxas.github.io/realitycheck/)
[![Build Status](https://travis-ci.org/imetaxas/realitycheck.svg?branch=master)](https://travis-ci.org/imetaxas/realitycheck)
[![Coverage Status](https://coveralls.io/repos/github/imetaxas/realitycheck/badge.svg?branch=master&service=github)](https://coveralls.io/github/imetaxas/realitycheck?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Reality Check
Reality Check is an open source Fluent Assertion framework for Java.

### Examples

#### Check files
  ```
  Reality.checkThat(file).isNull();
  Reality.checkThat(file).hasSameContentAs(file);
  ```
#### Check CSV files
  ```
  Reality.checkThatFileCsv(file1).hasSameContentAs(file2);
  Reality.checkThatFileCsv(file1).hasNotSameContentAs(file2);
  ```
#### Check CSV strings
  ```
  Reality.checkThatCsv(csvString1).hasSameContentAs(csvString2);
  Reality.checkThatCsv(csvString1).hasNotSameContentAs(csvString2);
  ```
#### Check strings
  ```
  Reality.checkThat(string).isNotNull();
  Reality.checkThat(string).hasLength(12);
  ```
#### Check InputStreams
  ```
  Reality.checkThat(new ByteArrayInputStream(byteArray1)).hasSameContentAs(new ByteArrayInputStream(byteArray2));
  Reality.checkThat(new ByteArrayInputStream(byteArray1)).hasNotSameContentAs(new ByteArrayInputStream(byteArray2));
  ```
#### Check with custom message
  ```
  Reality.checkWithMessage("File is NULL").that(file).isNotNull();
  Reality.checkWithMessage("String has wrong length").that(string).hasLength(6);
  Reality.checkWithMessage("InputStream is NULL").that(new ByteArrayInputStream(byteArray)).isNotNull();
  Reality.checkWithMessage("Files have different content").thatCsvFile(file).hasSameContentAs(file);
  ```
#### Do multiple checks
  ```
  Reality.checkThat(string).isNotNull().hasLength(12);
  Reality.checkThat(file1).hasSameContentAs(file1).hasNotSameContentAs(file2);
  ```
Coming checks...
-------
  *  Collections
  *  Deep & Shallow copies
  *  Dates
  *  Iterables
  *  POJO
  *  JSON
  *  Optionals
  *  Futures
  *  REST

Build
-------
mvn package


## Contributing
If you would like to help making this project better, see the [CONTRIBUTING.md](CONTRIBUTING.md).  

## Maintainers
Send any other comments and suggestions to [Yani Metaxas](https://github.com/imetaxas).

## License
This project is distributed under the [Apache-2.0 License](LICENSE).
