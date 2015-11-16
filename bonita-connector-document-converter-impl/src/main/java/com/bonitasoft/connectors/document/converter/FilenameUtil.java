/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

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
