package com.yanimetaxas.realitycheck.yaml;

import static com.yanimetaxas.realitycheck.yaml.YamlReality.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yanimetaxas.realitycheck.FailureHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class YamlCheckTest {

    static final String SAMPLE = """
            server:
              host: localhost
              port: 8080
            database:
              url: jdbc:postgresql://localhost/mydb
              pool-size: 10
            features:
              - auth
              - logging
              - metrics
            """;

    @Test
    void isValidYaml_passes() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).isValidYaml());
    }

    @Test
    void hasPath_topLevel() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).hasPath("server"));
    }

    @Test
    void hasPath_nested() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).hasPath("server.port"));
    }

    @Test
    void hasPath_failsForMissing() {
        assertThrows(AssertionError.class, () -> checkThatYaml(SAMPLE).hasPath("server.ssl"));
    }

    @Test
    void pathEquals_int() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).pathEquals("server.port", 8080));
    }

    @Test
    void pathEquals_string() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).pathEquals("server.host", "localhost"));
    }

    @Test
    void pathEquals_failsWithActualValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml(SAMPLE).pathEquals("server.port", 9090));
        assertTrue(e.getMessage().contains("8080"));
    }

    @Test
    void hasKey_passes() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).hasKey("server"));
    }

    @Test
    void pathIsList_passes() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).pathIsList("features"));
    }

    @Test
    void pathListHasSize_passes() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE).pathListHasSize("features", 3));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThatYaml(SAMPLE)
                .isValidYaml()
                .hasPath("server.host")
                .pathEquals("server.port", 8080)
                .pathEquals("database.pool-size", 10)
                .pathIsList("features")
                .pathListHasSize("features", 3));
    }

    @Test
    void navigatePath_mapListMapValue() {
        String yaml = """
                orgs:
                  - name: Spotify
                    teams:
                      - engineering
                      - design
                  - name: Google
                    teams:
                      - search
                """;
        assertDoesNotThrow(() -> checkThatYaml(yaml).pathEquals("orgs.0.name", "Spotify"));
        assertDoesNotThrow(() -> checkThatYaml(yaml).pathEquals("orgs.0.teams.1", "design"));
        assertDoesNotThrow(() -> checkThatYaml(yaml).pathEquals("orgs.1.name", "Google"));
    }

    @Test
    void navigatePath_mapListMapMap() {
        String yaml = """
                root:
                  items:
                    - meta:
                        key: deep-value
                """;
        assertDoesNotThrow(() ->
                checkThatYaml(yaml).pathEquals("root.items.0.meta.key", "deep-value"));
    }

    @Test
    void navigatePath_mapListMap_missingLeaf() {
        String yaml = """
                data:
                  - id: 1
                """;
        assertThrows(AssertionError.class,
                () -> checkThatYaml(yaml).hasPath("data.0.missing"));
    }

    @Test
    void pathIsNull_passesWhenPathDoesNotExist() {
        assertDoesNotThrow(() ->
                checkThatYaml("x: 1").pathIsNull("nonexistent"));
    }

    @Test
    void pathIsNull_passesWhenDeepPathDoesNotExist() {
        assertDoesNotThrow(() ->
                checkThatYaml("a:\n  b: 1").pathIsNull("a.c"));
    }

    @Test
    void pathIsNull_failWhenPathHasValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("key: hello").pathIsNull("key"));
        assertTrue(e.getMessage().contains("hello"));
    }

    @Test
    void pathIsNull_failWhenPathIsMap() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("obj:\n  k: v").pathIsNull("obj"));
    }

    @Test
    void pathIsNull_failWhenPathIsList() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("items:\n  - a").pathIsNull("items"));
    }

    @Test
    void hasKey_passOnMultiKeyRoot() {
        assertDoesNotThrow(() ->
                checkThatYaml("a: 1\nb: 2\nc: 3").hasKey("b"));
    }

    @Test
    void hasKey_failWhenKeyAbsent() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("a: 1\nb: 2").hasKey("z"));
        assertTrue(e.getMessage().contains("z"));
    }

    @Test
    void hasKey_failWhenRootIsScalar() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("just a string").hasKey("key"));
        assertTrue(e.getMessage().contains("map"));
    }

    @Test
    void pathEquals_failWrongStringValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("name: Alice").pathEquals("name", "Bob"));
        assertTrue(e.getMessage().contains("Alice"));
        assertTrue(e.getMessage().contains("Bob"));
    }

    @Test
    void pathEquals_failWrongIntValue() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("port: 8080").pathEquals("port", 9090));
    }

    @Test
    void pathEquals_failWrongBooleanValue() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("enabled: true").pathEquals("enabled", false));
    }

    @Test
    void pathIsList_failWhenMap() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("obj:\n  k: v").pathIsList("obj"));
        assertTrue(e.getMessage().contains("list"));
    }

    @Test
    void pathIsList_failWhenInteger() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("count: 42").pathIsList("count"));
    }

    @Test
    void pathIsList_failWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("x: 1").pathIsList("missing"));
    }

    @Test
    void pathEquals_booleanPass() {
        assertDoesNotThrow(() ->
                checkThatYaml("debug: true").pathEquals("debug", true));
    }

    @Test
    void pathEquals_floatPass() {
        assertDoesNotThrow(() ->
                checkThatYaml("ratio: 3.14").pathEquals("ratio", 3.14));
    }

    @Test
    void navigatePath_nestedLists() {
        String yaml = """
                matrix:
                  - - 1
                    - 2
                  - - 3
                    - 4
                """;
        assertDoesNotThrow(() -> checkThatYaml(yaml).pathEquals("matrix.0.0", 1));
        assertDoesNotThrow(() -> checkThatYaml(yaml).pathEquals("matrix.1.1", 4));
    }

    @Test
    void pathListHasSize_failWhenMap() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("obj:\n  k: v").pathListHasSize("obj", 1));
        assertTrue(e.getMessage().contains("list"));
    }

    @Test
    void fullChain_complexYaml() {
        String yaml = """
                server:
                  host: localhost
                  port: 8080
                  ssl: null
                features:
                  - auth
                  - logging
                """;
        assertDoesNotThrow(() -> checkThatYaml(yaml)
                .isValidYaml()
                .hasKey("server")
                .hasKey("features")
                .hasPath("server.host")
                .pathEquals("server.host", "localhost")
                .pathEquals("server.port", 8080)
                .pathIsNull("server.ssl")
                .pathIsList("features")
                .pathListHasSize("features", 2)
                .pathEquals("features.0", "auth"));
    }

    // ── Methods merged from YamlCheckCoverageTest ────────────────────────

    @Test
    void navigatePath_listIndexOutOfBounds_hasPathFails() {
        String yaml = "items:\n  - first\n  - second";
        assertThrows(AssertionError.class,
                () -> checkThatYaml(yaml).hasPath("items.99"));
    }

    @Test
    void navigatePath_nonNumericSegmentOnList_hasPathFails() {
        String yaml = "items:\n  - first";
        assertThrows(AssertionError.class,
                () -> checkThatYaml(yaml).hasPath("items.notANumber"));
    }

    @Test
    void navigatePath_throughScalar_returnsNull() {
        String yaml = "name: Alice";
        assertThrows(AssertionError.class,
                () -> checkThatYaml(yaml).hasPath("name.child"));
    }

    @Test
    void pathListHasSize_notAList() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("count: 5").pathListHasSize("count", 1));
        assertTrue(e.getMessage().contains("list"));
    }

    @Test
    void pathListHasSize_missingPath() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("a: 1").pathListHasSize("missing", 0));
    }

    @Test
    void hasKey_rootIsList_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("- a\n- b\n- c").hasKey("a"));
        assertTrue(e.getMessage().contains("map"));
    }

    @Test
    void hasKey_rootIsNull_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("---\n").hasKey("x"));
        assertTrue(e.getMessage().contains("null") || e.getMessage().contains("map"));
    }

    @Test
    void checkThatYamlFile_pass(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("config.yaml");
        Files.writeString(f, "server:\n  port: 8080\n  host: localhost");
        assertDoesNotThrow(() ->
                checkThatYamlFile(f).isValidYaml().hasPath("server.port").pathEquals("server.port", 8080));
    }

    @Test
    void checkThatYamlFile_missingFile(@TempDir Path dir) {
        assertThrows(java.io.UncheckedIOException.class,
                () -> checkThatYamlFile(dir.resolve("no-such.yaml")));
    }

    @Test
    void pathIsList_pass() {
        assertDoesNotThrow(() ->
                checkThatYaml("items:\n  - x\n  - y").pathIsList("items"));
    }

    @Test
    void pathIsList_failWhenScalar() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("items: notalist").pathIsList("items"));
        assertTrue(e.getMessage().contains("list"));
    }

    @Test
    void pathIsList_failWhenNull() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("items: null").pathIsList("items"));
        assertTrue(e.getMessage().contains("null"));
    }

    @Test
    void pathIsNull_pass() {
        assertDoesNotThrow(() -> checkThatYaml("val: null").pathIsNull("val"));
    }

    @Test
    void pathIsNull_failWhenNotNull() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("val: something").pathIsNull("val"));
    }

    @Test
    void pathEquals_wrongValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("x: hello").pathEquals("x", "world"));
        assertTrue(e.getMessage().contains("hello") || e.getMessage().contains("world"));
    }

    @Test
    void navigatePath_listIndexValid() {
        assertDoesNotThrow(() ->
                checkThatYaml("items:\n  - alpha\n  - beta").pathEquals("items.1", "beta"));
    }

    @Test
    void pathListHasSize_correctSize() {
        assertDoesNotThrow(() ->
                checkThatYaml("items:\n  - a\n  - b").pathListHasSize("items", 2));
    }

    @Test
    void pathListHasSize_wrongSize() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("items:\n  - a\n  - b").pathListHasSize("items", 5));
        assertTrue(e.getMessage().contains("5"));
    }

    @Test
    void isValidYaml_failsForInvalid() {
        String invalid = "key: [unclosed";
        assertThrows(AssertionError.class, () -> checkThatYaml(invalid).isValidYaml());
    }

    @Test
    void pathEquals_failsForMissingPath() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("x: 1").pathEquals("missing", "value"));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void pathEquals_failsForDeepMissingPath() {
        assertThrows(AssertionError.class,
                () -> checkThatYaml("a:\n  b: 1").pathEquals("a.c.d", "value"));
    }

    @Test
    void pathListHasSize_failsForMissingPath() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("x: 1").pathListHasSize("nonexistent", 0));
        assertTrue(e.getMessage().contains("list"));
    }

    @Test
    void pathIsList_failsForMissingPathExplicit() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("a: 1").pathIsList("nonexistent"));
        assertTrue(e.getMessage().contains("null"));
    }

    @Test
    void hasKey_failsForNullRoot() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatYaml("null").hasKey("any"));
        assertTrue(e.getMessage().contains("null") || e.getMessage().contains("map"));
    }

    // Soft-mode tests: exercise catch branch in parse()

    static class CollectingHandler extends FailureHandler {
        final List<String> messages = new ArrayList<>();

        @Override
        public void fail(String format, Object... args) {
            messages.add(args.length == 0 ? format : String.format(format, args));
        }
    }

    @Test
    void softMode_invalidYaml_continuesAfterParseFails() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("key: [unclosed", handler);
        check.isValidYaml();
        assertFalse(handler.messages.isEmpty());
        assertTrue(handler.messages.get(0).contains("invalid YAML"));
    }

    @Test
    void softMode_invalidYaml_hasPathContinues() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("key: [unclosed", handler);
        check.hasPath("any");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathEquals_missingPath() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("x: 1", handler);
        check.pathEquals("missing", "value");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathEquals_wrongValue() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("name: Alice", handler);
        check.pathEquals("name", "Bob");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathIsNull_nonNullValue() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("key: value", handler);
        check.pathIsNull("key");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathListHasSize_notAList() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("count: 5", handler);
        check.pathListHasSize("count", 1);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasKey_missingKey() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("a: 1", handler);
        check.hasKey("missing");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasKey_rootNotMap() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("- a\n- b", handler);
        check.hasKey("a");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathIsList_notAList() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("x: 1", handler);
        check.pathIsList("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_pathListHasSize_wrongSize() {
        var handler = new CollectingHandler();
        var check = new YamlCheck("items:\n  - a\n  - b", handler);
        check.pathListHasSize("items", 5);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void exceedsMaxSizeLimit_failsInSoftMode() {
        var handler = new CollectingHandler();
        String huge = "x: " + "a".repeat(11 * 1024 * 1024);
        var check = new YamlCheck(huge, handler);
        check.isValidYaml();
        assertFalse(handler.messages.isEmpty());
        assertTrue(handler.messages.get(0).contains("exceeds maximum allowed size"));
    }

    @Test
    void exceedsMaxSizeLimit_fails() {
        String huge = "x: " + "a".repeat(11 * 1024 * 1024);
        assertThrows(AssertionError.class, () -> checkThatYaml(huge).isValidYaml());
    }

    @Test
    void assertThatYaml_alias_works() {
        assertDoesNotThrow(() -> YamlReality.assertThatYaml(SAMPLE).isValidYaml());
    }

    @Test
    void assertThatYamlFile_alias_works(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.yaml");
        Files.writeString(file, SAMPLE);
        assertDoesNotThrow(() -> YamlReality.assertThatYamlFile(file).isValidYaml());
    }
}
