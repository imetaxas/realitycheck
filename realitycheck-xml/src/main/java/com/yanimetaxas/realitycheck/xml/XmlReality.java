package com.yanimetaxas.realitycheck.xml;

import com.yanimetaxas.realitycheck.FailureHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Entry point for XML assertions.
 */
public final class XmlReality {

    private XmlReality() {}

    public static XmlCheck checkThatXml(String xml) {
        return new XmlCheck(xml, new FailureHandler());
    }

    /** Alias for {@link #checkThatXml(String)} for AssertJ/Truth migration. */
    public static XmlCheck assertThatXml(String xml) {
        return checkThatXml(xml);
    }

    public static XmlCheck checkThatXmlFile(Path path) {
        try {
            return new XmlCheck(Files.readString(path), new FailureHandler());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Alias for {@link #checkThatXmlFile(Path)} for AssertJ/Truth migration. */
    public static XmlCheck assertThatXmlFile(Path path) {
        return checkThatXmlFile(path);
    }
}
