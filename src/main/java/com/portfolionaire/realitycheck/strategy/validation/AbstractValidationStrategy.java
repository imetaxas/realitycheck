package com.portfolionaire.realitycheck.strategy.validation;

import java.util.Optional;

/**
 * Created by imeta on 24-Oct-17.
 */
public abstract class AbstractValidationStrategy<T, K> implements ValidationStrategy<T, K> {

  Optional<T> actual;
  K actualValue;

  public AbstractValidationStrategy(T actual) {
    this.actual = Optional.ofNullable(actual);
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
