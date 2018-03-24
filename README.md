[![Build Status](https://travis-ci.org/imetaxas/realitycheck.svg?branch=master)](https://travis-ci.org/imetaxas/realitycheck)
[![CodeCov Coverage](https://codecov.io/gh/imetaxas/realitycheck/graph/badge.svg?branch=master)](https://codecov.io/gh/imetaxas/realitycheck?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.yanimetaxas/realitycheck/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.yanimetaxas/realitycheck/)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-green.svg)](http://makeapullrequest.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Reality Check
Reality Check is an open source Fluent Assertion framework for Java.

### Examples

#### Check files in system resources
  ```
  Reality.checkThatSystemResource(csvResource.getName()).exists();
  Reality.checkThatSystemResource(csvResource1).hasSameContentAs(resource1);
  ```
#### Check CSV files in system resources
  ```
  Reality.checkThatCsvResource(csvResource.getName()).exists();
  Reality.checkThatCsvResource(csvResource).doesNotExist();
  ```
#### Check files
  ```
  Reality.checkThat(file).isNull();
  Reality.checkThat(file).hasSameContentAs(file);
  Reality.checkThatFile(filePath).exists();
  ```
#### Check CSV files
  ```
  Reality.checkThatCsvFile(file1).hasSameContentAs(file1);
  Reality.checkThatCsvFile(file1).hasNotSameContentAs(file2);
  Reality.checkThatCsvFile(filename).headerHasNoDigits();
  ```
#### Check CSV strings
  ```
  Reality.checkThatCsv(csvString1).hasSameContentAs(csvString1);
  Reality.checkThatCsv(csvString1).hasNotSameContentAs(csvString2);
  ```
#### Check strings
  ```
  Reality.checkThat(string).isNotNull();
  Reality.checkThat(string).hasLength(12);
  ```
#### Check integers
  ```
  Reality.checkThat(1).isEqualTo(1);
  Reality.checkThat(1).isNotEqualTo(2);
  ```
#### Check booleans
  ```
  Reality.checkThat(true).isTrue();
  Reality.checkThat(false).isFalse();
  ```
#### Check InputStreams
  ```
  Reality.checkThat(new ByteArrayInputStream(byteArray1)).hasSameContentAs(new ByteArrayInputStream(byteArray1));
  Reality.checkThat(new ByteArrayInputStream(byteArray1)).hasNotSameContentAs(new ByteArrayInputStream(byteArray2));
  ```
#### Check with custom message
  ```
  Reality.checkWithMessage("File is NULL").that(file).isNotNull();
  Reality.checkWithMessage("String has wrong length").that(string).hasLength(6);
  Reality.checkWithMessage("InputStream is NULL").that(new ByteArrayInputStream(byteArray)).isNotNull();
  Reality.checkWithMessage("Files have different content").thatCsvFile(file).hasSameContentAs(file);
  Reality.checkWithMessage("Boolean is false").that(true).isTrue();
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


## Contributing
If you would like to help making this project better, see the [CONTRIBUTING.md](CONTRIBUTING.md).  

## Maintainers
Send any other comments and suggestions to [Yani Metaxas](https://github.com/imetaxas).

## License
This project is distributed under the [Apache-2.0 License](LICENSE).
