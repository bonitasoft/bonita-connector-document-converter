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

import org.bonitasoft.connectors.document.converter.core.DocToPDFConverter;
import org.junit.jupiter.api.Test;

class DocToPDFConverterTest {

    @Test
    void should_convert_docx_to_pdf() throws Exception {
        final DocToPDFConverter docxToPDFConverter = new DocToPDFConverter(DocToPDFConverterTest.class.getResourceAsStream("/patern-signets.docx"), "utf-8");

        final byte[] content = docxToPDFConverter.convert();

        assertThat(content).isNotEmpty();
    }
    
    @Test
    void should_convert_docx_to_pdf2() throws Exception {
        final DocToPDFConverter docxToPDFConverter = new DocToPDFConverter(DocToPDFConverterTest.class.getResourceAsStream("/QuoteRequestTemplate.docx"), "utf-8");

        final byte[] content = docxToPDFConverter.convert();

        assertThat(content).isNotEmpty();
    }

    @Test
    void should_convert_odt_to_pdf() throws Exception {
        final DocToPDFConverter docxToPDFConverter = new DocToPDFConverter(DocToPDFConverterTest.class.getResourceAsStream("/odtTest.odt"), "utf-8");

        final byte[] content = docxToPDFConverter.convert();

        assertThat(content).isNotEmpty();
    }

}
