package io.github.imetaxas.realitycheck.xml;

import static io.github.imetaxas.realitycheck.xml.XmlReality.*;
import static org.junit.jupiter.api.Assertions.*;

import io.github.imetaxas.realitycheck.FailureHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class XmlCheckTest {

    static final String SAMPLE = """
            <users>
              <user id="1">
                <name>Alice</name>
                <email>alice@example.com</email>
              </user>
              <user id="2">
                <name>Bob</name>
                <email>bob@example.com</email>
              </user>
            </users>
            """;

    @Test
    void isWellFormed_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).isWellFormed());
    }

    @Test
    void isWellFormed_failsForInvalid() {
        assertThrows(AssertionError.class, () -> checkThatXml("<broken>").isWellFormed());
    }

    @Test
    void hasRootElement_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).hasRootElement("users"));
    }

    @Test
    void hasRootElement_fails() {
        var e = assertThrows(AssertionError.class, () -> checkThatXml(SAMPLE).hasRootElement("people"));
        assertTrue(e.getMessage().contains("users"));
    }

    @Test
    void hasXPath_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).hasXPath("//user/name"));
    }

    @Test
    void xpathEquals_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).xpathEquals("//user[1]/name", "Alice"));
    }

    @Test
    void xpathEquals_failsWithActualValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(SAMPLE).xpathEquals("//user[1]/name", "Bob"));
        assertTrue(e.getMessage().contains("Alice"));
    }

    @Test
    void doesNotHaveXPath_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).doesNotHaveXPath("//user/phone"));
    }

    @Test
    void containsText_passes() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE).containsText("alice@example.com"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThatXml(SAMPLE)
                .isWellFormed()
                .hasRootElement("users")
                .hasXPath("//user[1]/name")
                .xpathEquals("//user[2]/email", "bob@example.com"));
    }

    static final String CATALOG_SAMPLE = """
            <catalog>
              <book id="b1" lang="en">
                <title>Effective Java</title>
                <author>Joshua Bloch</author>
                <price>45.00</price>
              </book>
              <book id="b2" lang="sv">
                <title>Clean Code</title>
                <author>Robert C. Martin</author>
                <price>39.99</price>
              </book>
              <empty/>
              <note attr="val"/>
            </catalog>
            """;

    @Test
    void hasXPath_attributeById() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE).hasXPath("//book/@id"));
    }

    @Test
    void hasXPath_specificAttributeValue() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE).hasXPath("//book[@id='b1']/title"));
    }

    @Test
    void hasXPath_attributeMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).hasXPath("//book/@isbn"));
    }

    @Test
    void xpathEquals_attributePass() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[1]/@id", "b1"));
    }

    @Test
    void xpathEquals_attributeFail() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[1]/@id", "wrong"));
        assertTrue(e.getMessage().contains("b1"));
        assertTrue(e.getMessage().contains("wrong"));
    }

    @Test
    void xpathEquals_langAttribute() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[2]/@lang", "sv"));
    }

    @Test
    void xpathEquals_langAttributeFail() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[2]/@lang", "en"));
    }

    @Test
    void hasXPath_emptyElementReturnsEmptyString_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).hasXPath("//empty/text()"));
    }

    @Test
    void doesNotHaveXPath_emptyElementNode_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).doesNotHaveXPath("//empty"));
    }

    @Test
    void doesNotHaveXPath_existingAttribute_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).doesNotHaveXPath("//book/@id"));
    }

    @Test
    void doesNotHaveXPath_missingAttribute_passes() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).doesNotHaveXPath("//book/@isbn"));
    }

    @Test
    void xpathEquals_deepNestedPass() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[@id='b2']/author", "Robert C. Martin"));
    }

    @Test
    void xpathEquals_deepNestedFail() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).xpathEquals("//book[@id='b1']/author", "Martin"));
    }

    @Test
    void hasXPath_withPredicate_pass() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).hasXPath("//book[@lang='en']/title"));
    }

    @Test
    void hasXPath_withPredicate_noMatch() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).hasXPath("//book[@lang='de']/title/text()"));
    }

    @Test
    void xpathEquals_noteAttribute() {
        assertDoesNotThrow(() ->
                checkThatXml(CATALOG_SAMPLE).xpathEquals("//note/@attr", "val"));
    }

    @Test
    void xpathEquals_noteAttributeWrong() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).xpathEquals("//note/@attr", "other"));
    }

    @Test
    void fullChainWithAttributes() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE)
                .isWellFormed()
                .hasRootElement("catalog")
                .hasXPath("//book/@id")
                .xpathEquals("//book[1]/@id", "b1")
                .xpathEquals("//book[2]/@lang", "sv")
                .doesNotHaveXPath("//book/@isbn")
                .containsText("Effective Java"));
    }

    @Test
    void containsText_rawAttributeInSource() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE).containsText("lang=\"en\""));
    }

    @Test
    void containsText_failsForAbsentAttributeValue() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).containsText("lang=\"de\""));
    }

    @Test
    void hasRootElement_afterOtherChecks() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE)
                .hasXPath("//book")
                .hasRootElement("catalog"));
    }

    @Test
    void hasRootElement_wrongAfterChain() {
        assertThrows(AssertionError.class, () -> checkThatXml(CATALOG_SAMPLE)
                .isWellFormed()
                .hasRootElement("books"));
    }

    @Test
    void parseDocumentCaching_multipleChecksReuseDocument() {
        assertDoesNotThrow(() -> checkThatXml(CATALOG_SAMPLE)
                .isWellFormed()
                .isWellFormed()
                .hasXPath("//book[1]/@id")
                .hasXPath("//book[2]/@id")
                .xpathEquals("//book[1]/title", "Effective Java")
                .xpathEquals("//book[2]/title", "Clean Code"));
    }

    @Test
    void doesNotHaveXPath_existingElement_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(CATALOG_SAMPLE).doesNotHaveXPath("//book"));
        assertTrue(e.getMessage().contains("2") || e.getMessage().contains("nodes"));
    }

    // ── Methods merged from XmlCheckCoverageTest ─────────────────────────

    static final String COVERAGE_SAMPLE = """
            <root attr="val">
              <item>text</item>
              <empty/>
              <nested><deep>found</deep></nested>
            </root>
            """;

    @Test
    void doesNotHaveXPath_passWhenAbsent() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).doesNotHaveXPath("//missing"));
    }

    @Test
    void doesNotHaveXPath_failWhenNodesFound() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).doesNotHaveXPath("//item"));
        assertTrue(e.getMessage().contains("match nothing"));
    }

    @Test
    void doesNotHaveXPath_failOnInvalidXPath() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).doesNotHaveXPath("///bad["));
    }

    @Test
    void containsText_pass() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).containsText("text"));
    }

    @Test
    void containsText_failWhenAbsent() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).containsText("NOT_PRESENT_STRING"));
        assertTrue(e.getMessage().contains("NOT_PRESENT_STRING"));
    }

    @Test
    void hasRootElement_pass() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).hasRootElement("root"));
    }

    @Test
    void hasRootElement_wrongName() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).hasRootElement("notRoot"));
        assertTrue(e.getMessage().contains("root"));
        assertTrue(e.getMessage().contains("notRoot"));
    }

    @Test
    void xpathEquals_pass() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).xpathEquals("//item", "text"));
    }

    @Test
    void xpathEquals_wrongValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).xpathEquals("//item", "wrong"));
        assertTrue(e.getMessage().contains("text"));
        assertTrue(e.getMessage().contains("wrong"));
    }

    @Test
    void xpathEquals_invalidXPath() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).xpathEquals("///[bad", "x"));
    }

    @Test
    void hasXPath_pass() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).hasXPath("//nested/deep"));
    }

    @Test
    void hasXPath_failWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).hasXPath("//nonexistent/text()"));
    }

    @Test
    void hasXPath_invalidExpression() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).hasXPath("///invalid["));
    }

    @Test
    void isWellFormed_pass() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).isWellFormed());
    }

    @Test
    void isWellFormed_failWithInvalidXml() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml("<unclosed>").isWellFormed());
        assertTrue(e.getMessage().contains("invalid XML"));
    }

    @Test
    void isWellFormed_failWithGarbage() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("this is not xml at all").isWellFormed());
    }

    @Test
    void checkThatXmlFile_pass(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("test.xml");
        Files.writeString(f, COVERAGE_SAMPLE);
        assertDoesNotThrow(() ->
                checkThatXmlFile(f).isWellFormed().hasRootElement("root").containsText("text"));
    }

    @Test
    void checkThatXmlFile_missingFile(@TempDir Path dir) {
        assertThrows(java.io.UncheckedIOException.class,
                () -> checkThatXmlFile(dir.resolve("missing.xml")));
    }

    @Test
    void fullChain() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE)
                .isWellFormed()
                .hasRootElement("root")
                .hasXPath("//item")
                .xpathEquals("//nested/deep", "found")
                .doesNotHaveXPath("//bogus")
                .containsText("found"));
    }

    @Test
    void xpathEquals_attributeValue() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).xpathEquals("//root/@attr", "val"));
    }

    @Test
    void xpathEquals_attributeWrongValue() {
        assertThrows(AssertionError.class,
                () -> checkThatXml(COVERAGE_SAMPLE).xpathEquals("//root/@attr", "wrong"));
    }

    @Test
    void hasXPath_attributeExists() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).hasXPath("//root/@attr"));
    }

    @Test
    void doesNotHaveXPath_attributeAbsent() {
        assertDoesNotThrow(() -> checkThatXml(COVERAGE_SAMPLE).doesNotHaveXPath("//root/@missing"));
    }

    @Test
    void xxe_attackIsBlocked() {
        String xxePayload = """
                <?xml version="1.0"?>
                <!DOCTYPE foo [
                  <!ENTITY xxe SYSTEM "file:///etc/passwd">
                ]>
                <root>&xxe;</root>
                """;
        assertThrows(AssertionError.class, () -> checkThatXml(xxePayload).isWellFormed());
    }

    @Test
    void hasRootElement_failsGracefullyOnInvalidXml() {
        assertThrows(AssertionError.class, () -> checkThatXml("not xml").hasRootElement("root"));
    }

    @Test
    void hasXPath_failsGracefullyOnInvalidXml() {
        assertThrows(AssertionError.class, () -> checkThatXml("not xml").hasXPath("//root"));
    }

    @Test
    void xpathEquals_failsGracefullyOnInvalidXml() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("not xml").xpathEquals("//root", "val"));
    }

    @Test
    void doesNotHaveXPath_failsGracefullyOnInvalidXml() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("not xml").doesNotHaveXPath("//root"));
    }

    @Test
    void containsText_failsForAbsentText() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml("<r/>").containsText("nope"));
        assertTrue(e.getMessage().contains("nope"));
    }

    @Test
    void hasRootElement_failsForWrongRoot() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("<actual/>").hasRootElement("expected"));
    }

    @Test
    void hasXPath_noMatch_failsWithMessage() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatXml("<r/>").hasXPath("//missing/text()"));
        assertTrue(e.getMessage().contains("no result"));
    }

    @Test
    void xpathEquals_invalidXPathExpression() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("<r/>").xpathEquals("[invalid", "x"));
    }

    @Test
    void doesNotHaveXPath_invalidXPathExpression() {
        assertThrows(AssertionError.class,
                () -> checkThatXml("<r/>").doesNotHaveXPath("[invalid"));
    }

    // Soft-mode tests covering doc==null branches after invalid XML parse

    static class CollectingHandler extends FailureHandler {
        final List<String> messages = new ArrayList<>();

        @Override
        public void fail(String format, Object... args) {
            messages.add(args.length == 0 ? format : String.format(format, args));
        }
    }

    @Test
    void softMode_hasXPath_returnsGracefullyWhenParseFailsWithoutThrowing() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("not xml", handler);
        check.hasXPath("//root");
        assertTrue(handler.messages.size() >= 1);
    }

    @Test
    void softMode_xpathEquals_returnsGracefullyWhenParseFailsWithoutThrowing() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("not xml", handler);
        check.xpathEquals("//root", "val");
        assertTrue(handler.messages.size() >= 1);
    }

    @Test
    void softMode_doesNotHaveXPath_returnsGracefullyWhenParseFailsWithoutThrowing() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("not xml", handler);
        check.doesNotHaveXPath("//root");
        assertTrue(handler.messages.size() >= 1);
    }

    @Test
    void softMode_hasRootElement_returnsGracefullyWhenParseFailsWithoutThrowing() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("not xml", handler);
        check.hasRootElement("root");
        assertTrue(handler.messages.size() >= 1);
    }

    @Test
    void softMode_hasXPath_noMatch() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root/>", handler);
        check.hasXPath("//missing/text()");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_xpathEquals_valueDiffers() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root><name>Alice</name></root>", handler);
        check.xpathEquals("//name", "Bob");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasRootElement_wrongName() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<actual/>", handler);
        check.hasRootElement("expected");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_containsText_absent() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root>hello</root>", handler);
        check.containsText("missing");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasXPath_invalidExpression() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root/>", handler);
        check.hasXPath("[invalid");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_xpathEquals_invalidExpression() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root/>", handler);
        check.xpathEquals("[invalid", "val");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_doesNotHaveXPath_nodesFound() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root><item/></root>", handler);
        check.doesNotHaveXPath("//item");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_doesNotHaveXPath_invalidExpression() {
        var handler = new CollectingHandler();
        var check = new XmlCheck("<root/>", handler);
        check.doesNotHaveXPath("[invalid");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void assertThatXml_alias_works() {
        assertDoesNotThrow(() -> XmlReality.assertThatXml("<root/>").isWellFormed());
    }

    @Test
    void assertThatXmlFile_alias_works(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.xml");
        Files.writeString(file, "<root/>");
        assertDoesNotThrow(() -> XmlReality.assertThatXmlFile(file).isWellFormed());
    }
}
