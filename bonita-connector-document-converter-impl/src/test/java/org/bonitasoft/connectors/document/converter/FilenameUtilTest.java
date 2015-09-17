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

public class FilenameUtilTest {

    @Test
    public void should_return_original_filename_with_pdf_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("", "myOriginalFileName.docx", "PDF");

        assertThat(outputFileName).isEqualTo("myOriginalFileName.pdf");
    }

    @Test
    public void should_return_original_filename_with_html_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName(null, "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("myOriginalFileName.html");
    }

    @Test
    public void should_return_new_filename_with_pdf_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName", "myOriginalFileName.docx", "PDF");

        assertThat(outputFileName).isEqualTo("aNewName.pdf");
    }

    @Test
    public void should_return_new_filename_with_html_extension() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName.odt", "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("aNewName.html");
    }

    @Test
    public void should_return_new_filename_with_html_extension2() throws Exception {
        final String outputFileName = FilenameUtil.toOutputFileName("aNewName.html", "myOriginalFileName.docx", "XHTML");

        assertThat(outputFileName).isEqualTo("aNewName.html");
    }

}
