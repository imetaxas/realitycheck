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
      throwProperAssertionError("Wrong length");
    }
    return self;
  }

  public StringAssert equalsIgnoreCase(String expected) {
    if(!actual.equalsIgnoreCase(expected)) {
      throwProperAssertionError("Not equal");
    }
    return self;
  }

  public StringAssert doesNotEqualIgnoreCase(String expected) {
    if(actual.equalsIgnoreCase(expected)) {
      throwProperAssertionError("Equals");
    }
    return self;
  }

  public StringAssert equals(String expected) {
    if(!actual.equals(expected)) {
      throwProperAssertionError("Not equal");
    }
    return self;
  }

  public StringAssert doesNotEqual(String expected) {
    if(actual.equals(expected)) {
      throwProperAssertionError("Equals");
    }
    return self;
  }

  public StringAssert startsWith(String expected) {
    if(!actual.startsWith(expected)) {
      throwProperAssertionError("Not start with");
    }
    return self;
  }

  public StringAssert endsWith(String expected) {
    if(!actual.endsWith(expected)) {
      throwProperAssertionError("Not end with");
    }
    return self;
  }

  public StringAssert contains(String expected) {
    if(!actual.contains(expected)) {
      throwProperAssertionError("Is not contained");
    }
    return self;
  }

  public StringAssert doesNotContain(String expected) {
    if(actual.contains(expected)) {
      throwProperAssertionError("Is contained");
    }
    return self;
  }

  public StringAssert isEmpty() {
    if(!actual.isEmpty()) {
      throwProperAssertionError("Is not empty");
    }
    return self;
  }

  public StringAssert isNotEmpty() {
    if(actual.isEmpty()) {
      throwProperAssertionError("Is empty");
    }
    return self;
  }

  public StringAssert matches(String regex) {
    if(!actual.matches(regex)) {
      throwProperAssertionError("Regex not matched");
    }
    return self;
  }

  public StringAssert doesNotMatch(String regex) {
    if(actual.matches(regex)) {
      throwProperAssertionError("Regex matched");
    }
    return self;
  }
}
