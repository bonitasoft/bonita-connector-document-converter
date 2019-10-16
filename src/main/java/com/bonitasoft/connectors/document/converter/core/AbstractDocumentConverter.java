/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.output.ByteArrayOutputStream;

import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public abstract class AbstractDocumentConverter implements DocumentConverter {

    private final InputStream inputStream;
    private final String encoding;

    public AbstractDocumentConverter(final InputStream inputStream, String encoding) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Source document InputStream cannot be null");
        }
        this.inputStream = inputStream;
        this.encoding = encoding;
    }

    @Override
    public byte[] convert() throws IOException, XDocReportException {
        final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
                inputStream, TemplateEngineKind.Velocity);
        final IContext context = report.createContext();
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            report.convert(context,
                    Options.getTo(getOutputType()).via(converterImplementation(report)).subOptions(getPDFOptions()), out);
            return out.toByteArray();
        }
    }

    protected PdfOptions getPDFOptions() {
        PdfOptions options = PdfOptions.create();
        if (encoding != null && !encoding.isEmpty()) {
            options = options.fontEncoding(encoding);
        }
        return options;
    }

    protected abstract ConverterTypeTo getOutputType();

    private ConverterTypeVia converterImplementation(final IXDocReport report) {
        return Objects.equals(report.getKind(), DocumentKind.ODT.name()) ? ConverterTypeVia.ODFDOM : converterTypeForDocx();
    }

    protected abstract ConverterTypeVia converterTypeForDocx();
}
