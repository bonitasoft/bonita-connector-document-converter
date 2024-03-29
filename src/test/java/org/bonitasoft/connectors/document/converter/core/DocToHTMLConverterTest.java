/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package org.bonitasoft.connectors.document.converter.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DocToHTMLConverterTest {

    @Test
    void should_throw_an_IllegalArgumentException_if_inputStream_is_null() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new DocToHTMLConverter(null, "utf-8"));
    }

    @Test
    void should_convert_docx_to_xhtml() throws Exception {
        final DocToHTMLConverter converter = new DocToHTMLConverter(
                DocToHTMLConverterTest.class.getResourceAsStream("/patern-signets.docx"), "utf-8");

        final byte[] content = converter.convert();

        assertThat(content).isNotEmpty();
    }

    @Test
    void should_convert_odt_to_xhtml() throws Exception {
        final DocToHTMLConverter converter = new DocToHTMLConverter(
                DocToHTMLConverterTest.class.getResourceAsStream("/odtTest.odt"), "utf-8");

        final byte[] content = converter.convert();

        assertThat(content).isNotEmpty();
    }

}
