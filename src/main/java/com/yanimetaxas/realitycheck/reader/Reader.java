package com.yanimetaxas.realitycheck.reader;

import com.yanimetaxas.realitycheck.strategy.validation.Action;
import java.io.IOException;

/**
 * @author yanimetaxas
 * @since 10-Dec-17
 */
public interface Reader<K> extends Action<K> {

  K read() throws IOException;
}
