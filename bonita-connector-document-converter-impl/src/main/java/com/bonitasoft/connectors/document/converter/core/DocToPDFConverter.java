/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter.core;

import java.io.InputStream;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;

public class DocToPDFConverter extends AbstractDocumentConverter {

    public DocToPDFConverter(final InputStream inputStream, String encoding) {
        super(inputStream, encoding);
    }

    @Override
    protected ConverterTypeTo getOutputType() {
        return ConverterTypeTo.PDF;
    }

    @Override
    protected ConverterTypeVia converterTypeForDocx() {
        return ConverterTypeVia.XWPF;
    }
}
