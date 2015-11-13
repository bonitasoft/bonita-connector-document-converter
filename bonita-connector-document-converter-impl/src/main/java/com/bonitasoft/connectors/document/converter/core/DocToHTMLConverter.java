/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter.core;

import java.io.InputStream;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;

public class DocToHTMLConverter extends AbstractDocumentConverter {

    public DocToHTMLConverter(final InputStream inputStream, String encoding) {
        super(inputStream, encoding);
    }

    @Override
    protected ConverterTypeTo getOutputType() {
        return ConverterTypeTo.XHTML;
    }

    /*
     * (non-Javadoc)
     * @see AbstractDocumentConverter#converterTypeForDocx()
     */
    @Override
    protected ConverterTypeVia converterTypeForDocx() {
        return ConverterTypeVia.XWPF;
    }

}
