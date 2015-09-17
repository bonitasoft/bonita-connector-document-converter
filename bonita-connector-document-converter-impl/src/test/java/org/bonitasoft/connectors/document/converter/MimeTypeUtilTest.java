/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
