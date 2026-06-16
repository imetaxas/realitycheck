# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - Unreleased

### Added

#### Core Assertions
- `StringCheck` — fluent string assertions (length, contains, starts/ends with, regex, case-insensitive)
- `NumberCheck` — numeric assertions with tolerance (`isCloseTo`, `isBetween`, `isPositive`)
- `BooleanCheck` — `isTrue`, `isFalse`
- `CollectionCheck` — size, contains, exact ordering, predicate matching
- `MapCheck` — key/entry containment, dot-path navigation for nested maps
- `OptionalCheck` — `isPresent`, `isEmpty`, `hasValue`
- `FutureCheck` — `completesWithin`, `hasValue`
- `FileCheck` — exists, extension, content diff, directory assertions (`isDirectoryContaining`, `hasDirectoryContentMatching`)
- `InputStreamCheck` — content assertions on streams
- `ObjectCheck` — generic `isNull`, `isNotNull`, `isInstanceOf`
- `ComparableCheck` — generic comparable assertions

#### Date/Time Assertions
- `InstantCheck` — before/after, closeTo with tolerance, past/future
- `LocalDateCheck` — year/month/day, weekday/weekend, leap year
- `LocalDateTimeCheck` — date + time component assertions
- `DurationCheck` — positive/negative, comparisons, millis/seconds/hours
- `ZonedDateTimeCheck` — zone-aware assertions, `hasZone`, `isCloseTo`, date/instant extractors
- `OffsetDateTimeCheck` — `hasOffset`, temporal comparisons, extractors

#### Exception Assertions
- `ThrowableCheck` — message assertions, cause chain traversal, root cause extraction
- Suppressed exception support: `hasSuppressed`, `hasSuppressedCount`, `suppressedException(index)`

#### Type-Specific Assertions
- `BigDecimalCheck` — scale, precision, `isEqualByComparingTo` (ignores scale), `isCloseTo` with tolerance
- `BigIntegerCheck` — `isEven`, `isOdd`, `isProbablePrime`, `hasBitLength`
- `UuidCheck` — `isNil`, `isVersion4`, `hasVersion`, string extractor
- `ByteArrayCheck` — `startsWith`, `toHex`, `toBase64` extractors
- `SealedClassCheck` — `isSealed`, `permits`, `permitsExactly`, permitted subclass extractors

#### Collection & Array Assertions
- `IterableCheck` — lazy iteration support for non-Collection iterables
- `ArrayCheck` — object array assertions with sorting, predicate matching
- `IntArrayCheck`, `LongArrayCheck`, `DoubleArrayCheck` — primitive array assertions with type-specific predicates
- `StreamCheck` — stream assertions with lazy materialization, `first()` and `toList()` extractors

#### Text & Format Assertions
- `MultilineCheck` — line count, first/last line, line-matching patterns, diff on failure
- `EnumCheck` — name, ordinal, `isOneOf`, `isNoneOf`
- `UriCheck` — scheme, host, port, path, query param, fragment assertions with extractors
- `CsvCheck` — row/column count, header validation, content diff
- `MatchResultCheck` — regex capture group assertions (by index and name) via `StringCheck.matchesAndCaptures`

#### Execution & Timing
- `ExecutionCheck` — `completesWithin`, `doesNotThrow`, `throwsInstanceOf` (subclass matching), `throwsExactly` (exact type), measured time extraction

#### Infrastructure
- `Diff` engine — LCS-based line diff with compact format for error messages
- `CheckFactory` — zero-boilerplate custom extension via `assertThat(value, MyCheck::new)`
- `FailureHandler` — custom messages (`checkWithMessage`) and contextual messages (`checkWithContext`)
- `SoftChecks` — collect all failures, report together (`assertAll(softly -> { ... })`)
- `@WithSoftChecks` JUnit 5 extension (in `realitycheck-junit5`)
- `RealityAssertions` — `assertThat()` aliases for migration from AssertJ/Truth
- `satisfies(Predicate, String)`, `matches(Predicate)`, and `satisfies(Consumer)` on base `Check` interface
- `isAtLeast(T)`, `isAtMost(T)`, `isIn(T, T)` aliases on `NumberCheck` and `ComparableCheck`
- IntelliJ live templates (`.idea/liveTemplates/RealityCheck.xml`) — `at`, `ct`, `ctb`, `ca`, `ctj`
- Automated migration script (`scripts/migrate.sh`) for bulk import replacement from AssertJ/Truth
- Javadoc publishing to GitHub Pages on every push to master

#### Data Format Modules
- `realitycheck-json` — JSON validation, dot-path queries, structural diff (requires Jackson)
- `realitycheck-xml` — XML well-formedness, XPath assertions (JDK only)
- `realitycheck-yaml` — YAML validation, dot-path queries (requires SnakeYAML)
- `realitycheck-snapshot` — golden-file snapshot testing with diff on mismatch
- `realitycheck-bom` — version alignment BOM

### Changed
- Minimum Java version: **17** (previously targeted 21; now compatible with Java 17 LTS)
- JaCoCo coverage threshold raised from 50% to **95%** (enforced per module)
- `assertThat()` aliases added to `SoftChecks` alongside existing `checkThat()` methods
- `assertWithMessage()` and `assertWithContext()` added to `RealityAssertions`
- CONTRIBUTING.md, issue templates, and PR template added
- Comparison table in README now includes JUnit 5 column
- Code coverage improved from ~59% to 95%+ across all modules

## [0.5.9] - 2018-03-24

Legacy single-module release (pre-rewrite).
