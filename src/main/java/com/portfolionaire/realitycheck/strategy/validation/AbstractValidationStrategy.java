package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by imeta on 24-Oct-17.
 */
public abstract class AbstractValidationStrategy<T> extends ArrayList<Action> implements ValidationStrategy<T> {

  private transient T actual;

  public AbstractValidationStrategy(T actual) {
    this.actual = actual;
  }

  public T getActualOrElse(T value) {
    return Optional.ofNullable(actual).orElse(value);
  }

  public T getActualOrThrow(ValidationException e) throws ValidationException {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  private Function<Void, T> toFunction(Action<T> action) {
    return x -> action.doAction();
  }

  @Override
  public byte[] validate() throws ValidationException {
    Iterator<Action> iterator = iterator();
    Function<Void, byte[]> function = null;
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
    return function.apply(null);
  }
}
