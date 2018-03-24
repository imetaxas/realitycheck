package com.yanimetaxas.realitycheck;

/**
 * @author yanimetaxas
 * @since 24-Mar-18
 */
public final class ObjectCheck extends AbstractCheck<ObjectCheck, Object> {

  ObjectCheck(Object object) {
    super(object, null);
  }

  ObjectCheck(Object object, String customMessage) {
    super(object, customMessage);
  }
}
