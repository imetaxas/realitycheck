# Contributing to Reality Check

Thank you for your interest in contributing! Here's how to get started.

## Development Setup

1. **Java 17+** and **Maven 3.5.4+** are required
2. Clone the repo and build:

```bash
git clone https://github.com/imetaxas/realitycheck.git
cd realitycheck
mvn clean verify
```

All 1,800+ tests should pass. JaCoCo enforces a minimum 95% line coverage per module.

## Making Changes

### Code Style

- Follow existing code formatting conventions
- No wildcard imports
- No `@author` tags in Javadoc (use git blame instead)
- All public methods must have Javadoc

### Tests

Every change must include tests. For new assertion methods, include both pass and fail cases:

```java
@Test
void myMethod_passes() {
    assertDoesNotThrow(() -> assertThat(value).myMethod(expected));
}

@Test
void myMethod_fails() {
    assertThrows(AssertionError.class, () -> assertThat(value).myMethod(wrong));
}
```

### Adding a New Check Class

1. Create `FooCheck extends AbstractCheck<FooCheck, Foo>` in `realitycheck-core`
2. Add `checkThat(Foo)` to `Reality.java`
3. Add `assertThat(Foo)` to `RealityAssertions.java`
4. Add `checkThat(Foo)` and `assertThat(Foo)` to `SoftChecks.java`
5. Create `FooCheckTest.java` with pass/fail tests for every method
6. Update `README.md` with usage examples

### Commit Messages

- Use imperative mood: "Add FooCheck" not "Added FooCheck"
- Keep the first line under 72 characters
- Reference issues: "Fix #123: Handle null input in StringCheck"

## Pull Request Process

1. Fork the repository
2. Create a feature branch from `master`
3. Make your changes with tests
4. Run `mvn clean verify` — all tests must pass, coverage must meet threshold
5. Open a PR against `master`
6. Describe what your change does and why

## Reporting Issues

Use [GitHub Issues](https://github.com/imetaxas/realitycheck/issues). Include:

- Reality Check version
- Java version
- Minimal reproducing test case
- Expected vs. actual behavior

## Code of Conduct

Be respectful and constructive. We're building testing tools — let's make them great together.
