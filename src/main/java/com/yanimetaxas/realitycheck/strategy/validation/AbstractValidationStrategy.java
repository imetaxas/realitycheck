package com.yanimetaxas.realitycheck.strategy.validation;

import com.yanimetaxas.realitycheck.exception.ValidationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author yanimetaxas
 */
abstract class AbstractValidationStrategy<T> extends ArrayList<Action> implements ValidationStrategy<T> {

  private transient T actual;

  AbstractValidationStrategy(T actual) {
    this.actual = actual;
  }

  T getActualOrElse(T value) {
    return Optional.ofNullable(actual).orElse(value);
  }

  T getActualOrThrow(ValidationException e) throws ValidationException {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  private Function<Void, T> toFunction(Action<T> action) {
    return x -> action.doAction();
  }

  @Override
  public byte[] validate() throws ValidationException {
    Iterator<Action> iterator = iterator();
    Function<Void, T> function = null;
    while (iterator.hasNext()) {
      function = toFunction(iterator.next());
      if(iterator.hasNext()){
        function.apply(null);
      } else {
        break;
      }
    }
    if(function == null) {
      return new byte[0];
    }
    return (byte[]) function.apply(null);
  }
}
