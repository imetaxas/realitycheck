package com.yanimetaxas.realitycheck.custom;

import com.yanimetaxas.realitycheck.asserter.CustomReadableObjectAssert;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author yanimetaxas
 * @since 18-Feb-18
 */
public class AbstractCustomReadableObject<ASSERT>  {

  public CustomReadableObjectAssert getAssertFromType() throws AssertionError {
    Type superclass;
    try {
      superclass = getClass().getGenericSuperclass();
      Type assertType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
      Class<?> assertRawType = (Class<?>) assertType;
      Type customObjectType = ((ParameterizedType) assertRawType.getGenericSuperclass()).getActualTypeArguments()[1];
      Class<?> customObjectRawType = (Class<?>) customObjectType;
      Constructor constructor = assertRawType.getConstructor(customObjectRawType);
      return (CustomReadableObjectAssert) constructor.newInstance(this);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
