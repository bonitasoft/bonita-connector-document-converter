/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;

public class MimeTypeUtil {

    public static String forFormat(final String format) {
        switch (ConverterTypeTo.valueOf(format)) {
            case PDF:
                return "application/pdf";
            case XHTML:
                return "application/xhtml+xml";
            default:
                return "text/plain";
        }
    }

}
