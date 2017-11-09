package com.portfolionaire.realitycheck.strategy.validation;

import com.portfolionaire.realitycheck.exception.ValidationException;
import java.util.Optional;

/**
 * Created by imeta on 24-Oct-17.
 */
public abstract class AbstractValidationStrategy<T> implements ValidationStrategy {

  private T actual;
  //Optional<K> actualValue;

  public AbstractValidationStrategy(T actual) {
    this.actual = actual;
  }

  public T getActualOrElse(T value) {
    return Optional.ofNullable(actual).orElse(value);
  }

  public T getActualOrThrow(ValidationException e) throws ValidationException {
    return Optional.ofNullable(actual).orElseThrow(() -> e);
  }

  /*Action<T, K>[] validationActions;

  List<Function<T, K>> functions;

  public AbstractValidationStrategy(Action<T, K>... validationActions) {
    for(Action action: validationActions) {
      functions.add((x) -> {
        try {
          return (K) action.doAction();
        } catch (ValidationException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }*/

  /*public AbstractValidationStrategy(Function[] functions) {
    this.functions = functions;
  }*/

  /*@Override
  public K validate() throws ValidationException {
    /*for (Action action: validationActions) {
      action.doAction();
    }*/

    /*Function<File, byte[]> c = (x) -> {
      try {
        return new FileReader(x).doAction();
      } catch (ValidationException e) {
        throw new RuntimeException(e);
      }
    };*/

    //Function<Action<File, byte[]>, byte[]> cc = null;
    //for (Action<File, byte[]> action : validationActions) {

    /*Function f = functions.get(0);
    for (int i = 1; i < functions.size(); i++) {
      f.andThen(functions.get(i));
    }
  }*/
}
