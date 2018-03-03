package com.yanimetaxas.realitycheck.asserter;

/**
 * @author yanimetaxas
 */
public class StringAssert extends AbstractAssert<StringAssert, String> {

  public StringAssert(String string) {
    super(string, null);
  }

  public StringAssert(String string, String message) {
    super(string, message);
  }

  public StringAssert hasLength(int size) throws AssertionError {
    if(actual.length() != size) {
      throwAssertionErrorWithCustomMessage("Wrong length");
    }
    return self;
  }

  public StringAssert equalsIgnoreCase(String expected) {
    if(!actual.equalsIgnoreCase(expected)) {
      throwAssertionErrorWithCustomMessage("Not equal");
    }
    return self;
  }

  public StringAssert doesNotEqualIgnoreCase(String expected) {
    if(actual.equalsIgnoreCase(expected)) {
      throwAssertionErrorWithCustomMessage("Equals");
    }
    return self;
  }

  public StringAssert equals(String expected) {
    if(!actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Not equal");
    }
    return self;
  }

  public StringAssert doesNotEqual(String expected) {
    if(actual.equals(expected)) {
      throwAssertionErrorWithCustomMessage("Equals");
    }
    return self;
  }

  public StringAssert startsWith(String expected) {
    if(!actual.startsWith(expected)) {
      throwAssertionErrorWithCustomMessage("Not start with");
    }
    return self;
  }

  public StringAssert endsWith(String expected) {
    if(!actual.endsWith(expected)) {
      throwAssertionErrorWithCustomMessage("Not end with");
    }
    return self;
  }

  public StringAssert contains(String expected) {
    if(!actual.contains(expected)) {
      throwAssertionErrorWithCustomMessage("Is not contained");
    }
    return self;
  }

  public StringAssert doesNotContain(String expected) {
    if(actual.contains(expected)) {
      throwAssertionErrorWithCustomMessage("Is contained");
    }
    return self;
  }

  public StringAssert isEmpty() {
    if(!actual.isEmpty()) {
      throwAssertionErrorWithCustomMessage("Is not empty");
    }
    return self;
  }

  public StringAssert isNotEmpty() {
    if(actual.isEmpty()) {
      throwAssertionErrorWithCustomMessage("Is empty");
    }
    return self;
  }

  public StringAssert matches(String regex) {
    if(!actual.matches(regex)) {
      throwAssertionErrorWithCustomMessage("Regex not matched");
    }
    return self;
  }

  public StringAssert doesNotMatch(String regex) {
    if(actual.matches(regex)) {
      throwAssertionErrorWithCustomMessage("Regex matched");
    }
    return self;
  }
}
