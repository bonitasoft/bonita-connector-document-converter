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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.output.ByteArrayOutputStream;

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

    public AbstractDocumentConverter(final InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Source document InputStream cannot be null");
        }
        this.inputStream = inputStream;
    }

    @Override
    public byte[] convert() throws IOException, XDocReportException {
        final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
                inputStream, TemplateEngineKind.Velocity);
        final IContext context = report.createContext();
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            report.convert(context, Options.getTo(getOutputType()).via(converterImplementation(report)), out);
            return out.toByteArray();
        }
    }

    protected abstract ConverterTypeTo getOutputType();

    private ConverterTypeVia converterImplementation(final IXDocReport report) {
        return Objects.equals(report.getKind(), DocumentKind.ODT.name()) ? ConverterTypeVia.ODFDOM : ConverterTypeVia.XWPF;
    }

}
