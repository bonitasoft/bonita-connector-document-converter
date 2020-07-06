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

import org.junit.Test;

public class MimeTypeUtilTest {

    @Test
    public void should_return_pdf_mime_type() throws Exception {
        assertThat(MimeTypeUtil.forFormat("PDF")).isEqualTo("application/pdf");
    }

    @Test
    public void should_return_html_mime_type() throws Exception {
        assertThat(MimeTypeUtil.forFormat("XHTML")).isEqualTo("application/xhtml+xml");
    }

    @Test
    public void should_return_plain_text_type_otherwise() throws Exception {
        assertThat(MimeTypeUtil.forFormat("FO")).isEqualTo("text/plain");
    }

}
