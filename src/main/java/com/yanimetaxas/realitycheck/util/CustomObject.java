package com.yanimetaxas.realitycheck.util;

import com.yanimetaxas.realitycheck.asserter.CustomReadableObjectAssert;

/**
 * Created by imeta on 09-Nov-17.
 */
public class CustomObject extends AbstractCustomObject<CustomReadableObjectAssert> {

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
