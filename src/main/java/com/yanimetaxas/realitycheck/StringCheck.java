package com.yanimetaxas.realitycheck;

/**
 * @author yanimetaxas
 */
public final class StringCheck extends AbstractCheck<StringCheck, String> {

  StringCheck(String string) {
    super(string, null);
  }

  StringCheck(String string, String message) {
    super(string, message);
  }

  public StringCheck hasLength(int size) throws AssertionError {
    if(actual.length() != size) {
      throwAssertionErrorWithCustomMessage("Wrong length");
    }
    return self;
  }

  public StringCheck equalsIgnoreCase(String expected) {
    if(!actual.equalsIgnoreCase(expected)) {
      throwAssertionErrorWithCustomMessage("Not equal");
    }
    return self;
  }

  public StringCheck doesNotEqualIgnoreCase(String expected) {
    if(actual.equalsIgnoreCase(expected)) {
      throwAssertionErrorWithCustomMessage("Equals");
    }
    return self;
  }

  public StringCheck equals(String expected) {
    if(!actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Not equal");
    }
    return self;
  }

  public StringCheck doesNotEqual(String expected) {
    if(actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Equals");
    }
    return self;
  }

  public StringCheck startsWith(String expected) {
    if(!actual.startsWith(expected)) {
      throwAssertionErrorWithCustomMessage("Not start with");
    }
    return self;
  }

  public StringCheck endsWith(String expected) {
    if(!actual.endsWith(expected)) {
      throwAssertionErrorWithCustomMessage("Not end with");
    }
    return self;
  }

  public StringCheck contains(String expected) {
    if(!actual.contains(expected)) {
      throwAssertionErrorWithCustomMessage("Is not contained");
    }
    return self;
  }

  public StringCheck doesNotContain(String expected) {
    if(actual.contains(expected)) {
      throwAssertionErrorWithCustomMessage("Is contained");
    }
    return self;
  }

  public StringCheck isEmpty() {
    if(!actual.isEmpty()) {
      throwAssertionErrorWithCustomMessage("Is not empty");
    }
    return self;
  }

  public StringCheck isNotEmpty() {
    if(actual.isEmpty()) {
      throwAssertionErrorWithCustomMessage("Is empty");
    }
    return self;
  }

  public StringCheck matches(String regex) {
    if(!actual.matches(regex)) {
      throwAssertionErrorWithCustomMessage("Regex not matched");
    }
    return self;
  }

  public StringCheck doesNotMatch(String regex) {
    if(actual.matches(regex)) {
      throwAssertionErrorWithCustomMessage("Regex matched");
    }
    return self;
  }
}
