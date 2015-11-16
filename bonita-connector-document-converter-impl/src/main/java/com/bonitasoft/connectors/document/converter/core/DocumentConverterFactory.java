/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter.core;

import java.io.ByteArrayInputStream;

public class DocumentConverterFactory {

    public static final String PDF = "PDF";
    public static final String XHTML = "XHTML";

    public DocumentConverter newConverter(final ByteArrayInputStream is, final String outputFormat, String encoding) {
        switch (outputFormat) {
            case PDF:
                return new DocToPDFConverter(is, encoding);
            case XHTML:
                return new DocToHTMLConverter(is, encoding);
            default:
                throw new IllegalArgumentException(String.format("Unknown output format: %s. Supported formats are %s, %s", outputFormat, PDF, XHTML));
        }

    }

}
