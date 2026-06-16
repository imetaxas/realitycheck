package com.yanimetaxas.realitycheck.xml;

import com.yanimetaxas.realitycheck.AbstractCheck;
import com.yanimetaxas.realitycheck.FailureHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * Fluent assertions for XML strings. Uses JDK built-in XML parsers (no external deps).
 *
 * <pre>{@code
 * import static com.yanimetaxas.realitycheck.xml.XmlReality.*;
 *
 * checkThatXml(response).isWellFormed().hasXPath("//user/name").xpathEquals("//user/name", "Alice");
 * }</pre>
 */
public final class XmlCheck extends AbstractCheck<XmlCheck, String> {

    private static final DocumentBuilderFactory DOC_FACTORY;
    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

    static {
        try {
            DOC_FACTORY = DocumentBuilderFactory.newInstance();
            DOC_FACTORY.setNamespaceAware(true);
            DOC_FACTORY.setXIncludeAware(false);
            DOC_FACTORY.setExpandEntityReferences(false);
            DOC_FACTORY.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DOC_FACTORY.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DOC_FACTORY.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DOC_FACTORY.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DOC_FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Document document;

    XmlCheck(String actual, FailureHandler handler) {
        super(actual, handler);
    }

    public XmlCheck isWellFormed() {
        parseDocument();
        return self();
    }

    public XmlCheck hasXPath(String expression) {
        Document doc = parseDocument();
        if (doc == null) return self();
        try {
            var xpath = XPATH_FACTORY.newXPath();
            String result = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
            if (result == null || result.isEmpty()) {
                failureHandler().fail("expected XPath <%s> to match but it returned no result",
                        expression);
            }
        } catch (XPathExpressionException e) {
            failureHandler().fail("invalid XPath expression <%s>: %s",
                    expression, e.getMessage());
        }
        return self();
    }

    public XmlCheck xpathEquals(String expression, String expectedValue) {
        Document doc = parseDocument();
        if (doc == null) return self();
        try {
            var xpath = XPATH_FACTORY.newXPath();
            String result = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
            if (!expectedValue.equals(result)) {
                failureHandler().fail("expected XPath <%s> = <%s> but was: <%s>",
                        expression, expectedValue, result);
            }
        } catch (XPathExpressionException e) {
            failureHandler().fail("invalid XPath expression <%s>: %s",
                    expression, e.getMessage());
        }
        return self();
    }

    public XmlCheck doesNotHaveXPath(String expression) {
        Document doc = parseDocument();
        if (doc == null) return self();
        try {
            var xpath = XPATH_FACTORY.newXPath();
            var nodeList = (org.w3c.dom.NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                failureHandler().fail("expected XPath <%s> to match nothing but found <%d> nodes",
                        expression, nodeList.getLength());
            }
        } catch (XPathExpressionException e) {
            failureHandler().fail("invalid XPath expression <%s>: %s",
                    expression, e.getMessage());
        }
        return self();
    }

    public XmlCheck hasRootElement(String expectedName) {
        Document doc = parseDocument();
        if (doc == null) return self();
        String rootName = doc.getDocumentElement().getTagName();
        if (!rootName.equals(expectedName)) {
            failureHandler().fail("expected root element <%s> but was: <%s>",
                    expectedName, rootName);
        }
        return self();
    }

    public XmlCheck containsText(String text) {
        if (!actual().contains(text)) {
            failureHandler().fail("expected XML to contain text <%s>",
                    text);
        }
        return self();
    }

    private Document parseDocument() {
        if (document != null) return document;
        try {
            var builder = DOC_FACTORY.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(actual())));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            failureHandler().fail("invalid XML: %s",
                    e.getMessage());
        }
        return document;
    }
}
