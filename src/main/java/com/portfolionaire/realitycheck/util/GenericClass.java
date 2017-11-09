package com.portfolionaire.realitycheck.util;

/**
 * Workaround for getting the generic type of class at runtime
 *
 * @author yanimetaxas
 */
public class GenericClass<T> {

  private final Class<T> type;

  public GenericClass(Class<T> type) {
    this.type = type;
  }

  public Class<T> getType() {
    return this.type;
  }
}
