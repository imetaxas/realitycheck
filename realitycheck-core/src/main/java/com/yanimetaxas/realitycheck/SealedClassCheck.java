package com.yanimetaxas.realitycheck;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fluent assertions for sealed classes/interfaces (Java 17+).
 *
 * <pre>{@code
 * checkThatSealed(Shape.class).isSealed().permittedCount(3);
 * checkThatSealed(Shape.class).permits(Circle.class);
 * checkThatSealed(Shape.class).permitsExactly(Circle.class, Square.class, Triangle.class);
 * }</pre>
 */
public final class SealedClassCheck extends AbstractCheck<SealedClassCheck, Class<?>> {

    SealedClassCheck(Class<?> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public SealedClassCheck isSealed() {
        return failureHandler().check(self(), actual().isSealed(),
                "expected <%s> to be sealed but it is not", actual().getName());
    }

    public SealedClassCheck isNotSealed() {
        return failureHandler().check(self(), !actual().isSealed(),
                "expected <%s> not to be sealed but it is", actual().getName());
    }

    public SealedClassCheck permittedCount(int expected) {
        ensureSealed();
        int count = actual().getPermittedSubclasses().length;
        return failureHandler().check(self(), count == expected,
                "expected <%d> permitted subclasses but was: <%d>",
                expected, count);
    }

    public SealedClassCheck permits(Class<?> subclass) {
        ensureSealed();
        boolean found = false;
        for (Class<?> p : actual().getPermittedSubclasses()) {
            if (p.equals(subclass)) { found = true; break; }
        }
        if (!found) {
            failureHandler().fail("expected <%s> to permit <%s> but permitted: %s",
                    actual().getName(), subclass.getName(), permittedNames());
        }
        return self();
    }

    public SealedClassCheck permitsExactly(Class<?>... subclasses) {
        ensureSealed();
        Set<Class<?>> expected = Set.of(subclasses);
        Set<Class<?>> actual = Set.of(actual().getPermittedSubclasses());
        return failureHandler().check(self(), actual.equals(expected),
                "expected permitted subclasses %s but was: %s",
                classNames(subclasses), permittedNames());
    }

    /** Extracts the permitted subclasses as a collection for further assertions. */
    public CollectionCheck<Class<?>> permittedSubclasses() {
        ensureSealed();
        return new CollectionCheck<>(
                Arrays.asList(actual().getPermittedSubclasses()), failureHandler());
    }

    private void ensureSealed() {
        if (!actual().isSealed()) {
            failureHandler().fail("<%s> is not sealed — cannot inspect permitted subclasses",
                    actual().getName());
        }
    }

    private String permittedNames() {
        return Arrays.stream(actual().getPermittedSubclasses())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String classNames(Class<?>[] classes) {
        return Arrays.stream(classes)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
