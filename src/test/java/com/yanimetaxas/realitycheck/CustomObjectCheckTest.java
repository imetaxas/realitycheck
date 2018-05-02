package com.yanimetaxas.realitycheck;

import com.yanimetaxas.realitycheck.custom.CustomObject;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class CustomObjectCheckTest {

  @Test
  public void isNotNull() throws Exception {
    new CustomObjectCheck(new CustomObject()).isNotNull();
  }

}