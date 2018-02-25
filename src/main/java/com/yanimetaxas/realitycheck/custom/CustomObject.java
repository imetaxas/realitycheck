package com.yanimetaxas.realitycheck.custom;

import com.yanimetaxas.realitycheck.asserter.CustomObjectAssert;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CustomObject extends AbstractCustomObject<CustomObjectAssert> {

  private String string;
  private int integer;

  public CustomObject(String string, int integer) {
    this.string = string;
    this.integer = integer;
  }

  public String getString() {
    return string;
  }

  public int getInteger() {
    return integer;
  }


}
