package com.portfolionaire.realitycheck.util;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imeta on 21-Sep-17.
 */
public class IoUtil {

  private IoUtil() {
  }

  @SuppressWarnings("ConstantConditions")
  public static File toFile(String filename) throws Exception {
    ClassLoader classLoader = IoUtil.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return new File(url.getFile());
  }

  public static String readFirstLine(byte[] bytes) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    return bufferedReader.readLine();
  }

  @SuppressWarnings("ConstantConditions")
  public static File loadResource(String filename) {
    ClassLoader classLoader = IoUtil.class.getClassLoader();
    URL url = classLoader.getResource(filename);
    return new File(url.getFile());
  }

  public static boolean areInputStreamsEqual(InputStream i1, InputStream i2) throws IOException {
    ReadableByteChannel ch1 = Channels.newChannel(i1);
    ReadableByteChannel ch2 = Channels.newChannel(i2);
    ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
    ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);
    try {
      while (true) {

        int n1 = ch1.read(buf1);
        int n2 = ch2.read(buf2);

        if (n1 == -1 || n2 == -1) {
          return n1 == n2;
        }

        buf1.flip();
        buf2.flip();

        for (int i = 0; i < Math.min(n1, n2); i++) {
          if (buf1.get() != buf2.get()) {
            return false;
          }
        }

        buf1.compact();
        buf2.compact();
      }

    } finally {
      i1.close();
      i2.close();
      ch1.close();
      ch2.close();
    }
  }
}
