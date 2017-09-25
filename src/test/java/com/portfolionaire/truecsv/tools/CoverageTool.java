package com.portfolionaire.truecsv.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.Assert;

/**
 * @author yanimetaxas
 */
public class CoverageTool {

  public static void testPrivateConstructor(Class<?> c) throws Exception {
    Constructor<?> declaredConstructor = c.getDeclaredConstructors()[0];
    declaredConstructor.setAccessible(true);
    declaredConstructor.newInstance();
  }
}

