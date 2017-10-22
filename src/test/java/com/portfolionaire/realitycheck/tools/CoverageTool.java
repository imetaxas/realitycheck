package com.portfolionaire.realitycheck.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.Assert;

/**
 * @author yanimetaxas
 */
public class CoverageTool {

  public static void testProtectedConstructor(Class<?> c) {
    Constructor<?>[] declaredConstructors = c.getDeclaredConstructors();
    Assert
        .assertEquals("Class does has have 1 declared constructor", 1, declaredConstructors.length);
    Constructor<?> declaredConstructor = declaredConstructors[0];
    Assert.assertEquals("Constructor should be private", Modifier.PROTECTED, Modifier.PROTECTED);

    declaredConstructor.setAccessible(true);
  }
}

