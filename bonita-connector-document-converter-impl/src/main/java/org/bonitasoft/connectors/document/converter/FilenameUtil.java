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

public class FilenameUtil {

    public static String toOutputFileName(String filename, final String originalFileName, final String outputFormat) {
        filename = filename == null || filename.isEmpty() ? originalFileName : filename;
        if (filename.endsWith(".docx")) {
            filename = filename.replace(".docx", fileExtension(outputFormat));
        } else if (filename.endsWith(".odt")) {
            filename = filename.replace(".odt", fileExtension(outputFormat));
        } else if (!filename.endsWith(fileExtension(outputFormat))) {
            filename = filename + "." + outputFormat.toLowerCase();
        }
        return filename;
    }

    private static String fileExtension(final String outputFormat) {
        return outputFormat.equals("XHTML") ? ".html" : "." + outputFormat.toLowerCase();
    }

}
