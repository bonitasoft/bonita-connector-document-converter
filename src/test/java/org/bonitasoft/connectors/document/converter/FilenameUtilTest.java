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

import org.junit.jupiter.api.Test;


class FilenameUtilTest {

    @Test
    void should_return_original_filename_with_pdf_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("", "myOriginalFileName.docx", "PDF");

        assertThat(outputFileName).isEqualTo("myOriginalFileName.pdf");
    }

    @Test
    void should_return_original_filename_with_html_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName(null, "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("myOriginalFileName.html");
    }

    @Test
    void should_return_new_filename_with_pdf_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName", "myOriginalFileName.docx", "PDF");

        assertThat(outputFileName).isEqualTo("aNewName.pdf");
    }

    @Test
    void should_return_new_filename_with_html_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName.odt", "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("aNewName.html");
    }

    @Test
    void should_return_new_filename_with_html_extension2() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName.html", "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("aNewName.html");
    }

}
