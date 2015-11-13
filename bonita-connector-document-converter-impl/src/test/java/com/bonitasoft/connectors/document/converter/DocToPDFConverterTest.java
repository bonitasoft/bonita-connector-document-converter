/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.bonitasoft.connectors.document.converter.core.DocToPDFConverter;

public class DocToPDFConverterTest {

    @Test
    public void should_convert_docx_to_pdf() throws Exception {
        final DocToPDFConverter docxToPDFConverter = new DocToPDFConverter(DocToPDFConverterTest.class.getResourceAsStream("/patern-signets.docx"), "utf-8");

        final byte[] content = docxToPDFConverter.convert();

        assertThat(content).isNotEmpty();
    }

    @Test
    public void should_convert_odt_to_pdf() throws Exception {
        final DocToPDFConverter docxToPDFConverter = new DocToPDFConverter(DocToPDFConverterTest.class.getResourceAsStream("/odtTest.odt"), "utf-8");

        final byte[] content = docxToPDFConverter.convert();

        assertThat(content).isNotEmpty();
    }

}
