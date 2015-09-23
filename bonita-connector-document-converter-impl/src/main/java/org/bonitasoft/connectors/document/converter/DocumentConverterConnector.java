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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.document.converter.core.DocumentConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.core.XDocReportException;

public class DocumentConverterConnector extends AbstractConnector {

    static final String SOURCE_DOCUMENT = "sourceDocument";
    static final String OUTPUT_FORMAT = "outputFormat";
    static final String OUTPUT_FILE_NAME = "outputFileName";
    static final String OUTPUT_DOCUMENT_VALUE = "ouptutDocumentValue";
    private final DocumentConverterFactory documentConverterFactory;

    public DocumentConverterConnector() {
        super();
        documentConverterFactory = new DocumentConverterFactory();
    }

    DocumentConverterConnector(final DocumentConverterFactory documentConverterFactory) {
        this.documentConverterFactory = documentConverterFactory;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.connector.AbstractConnector#executeBusinessLogic()
     */
    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        final Logger logger = LoggerFactory.getLogger(DocumentConverterConnector.class);
        final ProcessAPI processAPI = getAPIAccessor().getProcessAPI();
        Document document;
        try {
            document = loadDocument(processAPI);
        } catch (final DocumentNotFoundException e) {
            throw new ConnectorException(e);
        }
        byte[] content;
        try {
            content = processAPI.getDocumentContent(document.getContentStorageId());
        } catch (final DocumentNotFoundException e) {
            throw new ConnectorException(e);
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            final DocumentConverter converter = documentConverterFactory.newConverter(is, getOutputFormat());

            final long time = System.currentTimeMillis();
            logger.info(String.format("Converting %s to %s...", document.getContentFileName(), getOutputFormat()));
            final byte[] newContent = converter.convert();
            logger.info(String.format("Convertion done in %s ms", System.currentTimeMillis() - time));
            setOutputParameter(OUTPUT_DOCUMENT_VALUE,
                    createDocumentValue(newContent,
                            MimeTypeUtil.forFormat(getOutputFormat()),
                            FilenameUtil.toOutputFileName((String) getInputParameter(OUTPUT_FILE_NAME), document.getContentFileName(), getOutputFormat())));
        } catch (final IOException | XDocReportException e) {
            throw new ConnectorException(e);
        }
    }

    private Document loadDocument(final ProcessAPI processAPI) throws DocumentNotFoundException {
        final long processInstanceId = getExecutionContext().getProcessInstanceId();
        final Document document = processAPI.getLastDocument(processInstanceId, getSourceDocumentReference());
        return document;
    }

    private String getSourceDocumentReference() {
        return (String) getInputParameter(SOURCE_DOCUMENT);
    }

    private String getOutputFormat() {
        return (String) getInputParameter(OUTPUT_FORMAT, ConverterTypeTo.PDF.name());
    }

    private DocumentValue createDocumentValue(final byte[] content, final String mimeType, final String outputFileName) {
        return new DocumentValue(content, mimeType, outputFileName);
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.connector.Connector#validateInputParameters()
     */
    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        new InputParametersValidator(copyInputParameters()).validateInputParameters();
    }

    private Map<String, Object> copyInputParameters() {
        final Map<String, Object> inputParameters = new HashMap<>();
        inputParameters.put(OUTPUT_FILE_NAME, getInputParameter(OUTPUT_FILE_NAME));
        inputParameters.put(SOURCE_DOCUMENT, getInputParameter(SOURCE_DOCUMENT));
        inputParameters.put(OUTPUT_FORMAT, getInputParameter(OUTPUT_FORMAT));
        return inputParameters;
    }

}
