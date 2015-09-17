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

import java.io.ByteArrayInputStream;

import org.bonitasoft.connectors.document.converter.core.DocToHTMLConverter;
import org.bonitasoft.connectors.document.converter.core.DocToPDFConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DocumentConverterFactoryTest {

    @Rule
    public ExpectedException expectedExcpetion = ExpectedException.none();

    @Test
    public void should_create_a_toPDF_converter() throws Exception {
        final DocumentConverter pdfConverter = new DocumentConverterFactory().newConverter(new ByteArrayInputStream(new byte[0]), "PDF");

        assertThat(pdfConverter).isInstanceOf(DocToPDFConverter.class);
    }

    @Test
    public void should_create_a_toHTML_converter() throws Exception {
        final DocumentConverter pdfConverter = new DocumentConverterFactory().newConverter(new ByteArrayInputStream(new byte[0]), "XHTML");

        assertThat(pdfConverter).isInstanceOf(DocToHTMLConverter.class);
    }

    @Test
    public void should_throw_an_IllegalArgumentException_if_format_is_unknown() throws Exception {
        expectedExcpetion.expect(IllegalArgumentException.class);

        new DocumentConverterFactory().newConverter(new ByteArrayInputStream(new byte[0]), "PPT");
    }
}
