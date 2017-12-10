package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    AbstractValidationStrategy<?> that = (AbstractValidationStrategy<?>) o;

    return actual != null ? actual.equals(that.actual) : that.actual == null;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (actual != null ? actual.hashCode() : 0);
    return result;
  }
}
