/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package org.bonitasoft.connectors.document.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;

import org.bonitasoft.connectors.document.converter.core.DocToHTMLConverter;
import org.bonitasoft.connectors.document.converter.core.DocToPDFConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;
import org.junit.jupiter.api.Test;

class DocumentConverterFactoryTest {

    @Test
    void should_create_a_toPDF_converter() throws Exception {
        final DocumentConverter pdfConverter = new DocumentConverterFactory().newConverter(new ByteArrayInputStream(new byte[0]), "PDF", "utf-8");

        assertThat(pdfConverter).isInstanceOf(DocToPDFConverter.class);
    }

    @Test
    void should_create_a_toHTML_converter() throws Exception {
        final DocumentConverter pdfConverter = new DocumentConverterFactory().newConverter(new ByteArrayInputStream(new byte[0]), "XHTML", "utf-8");

        assertThat(pdfConverter).isInstanceOf(DocToHTMLConverter.class);
    }

    @Test
    void should_throw_an_IllegalArgumentException_if_format_is_unknown() throws Exception {
        DocumentConverterFactory documentConverterFactory = new DocumentConverterFactory();
        assertThrows(IllegalArgumentException.class,
                () -> documentConverterFactory.newConverter(null, "PPT", "utf-8"),
                "Supported formats are PDF, XHTML");
    }
}
