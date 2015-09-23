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
package org.bonitasoft.connectors.document.converter.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.connectors.document.converter.DocToPDFConverterTest;
import org.bonitasoft.connectors.document.converter.core.DocToHTMLConverter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DocToHTMLConverterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_throw_an_IllegalArgumentException_if_inputStream_is_null() throws Exception {
        expectedException.expect(IllegalArgumentException.class);

        new DocToHTMLConverter(null, "utf-8");
    }

    @Test
    public void should_convert_docx_to_xhtml() throws Exception {
        final DocToHTMLConverter converter = new DocToHTMLConverter(DocToPDFConverterTest.class.getResourceAsStream("/patern-signets.docx"), "utf-8");

        final byte[] content = converter.convert();

        assertThat(content).isNotEmpty();
    }

    @Test
    public void should_convert_odt_to_xhtml() throws Exception {
        final DocToHTMLConverter converter = new DocToHTMLConverter(DocToPDFConverterTest.class.getResourceAsStream("/odtTest.odt"), "utf-8");

        final byte[] content = converter.convert();

        assertThat(content).isNotEmpty();
    }

}
