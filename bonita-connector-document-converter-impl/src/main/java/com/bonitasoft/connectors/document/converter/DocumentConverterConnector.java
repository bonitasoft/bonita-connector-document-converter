/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonitasoft.connectors.document.converter.core.DocumentConverter;
import com.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.core.XDocReportException;

public class DocumentConverterConnector extends AbstractConnector {

    private static final String DEFAULT_ENCODING = "utf-8";
    static final String SOURCE_DOCUMENT = "sourceDocument";
    static final String ENCODING = "encoding";
    static final String OUTPUT_FORMAT = "outputFormat";
    static final String OUTPUT_FILE_NAME = "outputFileName";
    static final String OUTPUT_DOCUMENT_VALUE = "outputDocumentValue";
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
            final DocumentConverter converter = documentConverterFactory.newConverter(is, getOutputFormat(), getEncoding());

            final long time = System.currentTimeMillis();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Converting %s to %s...", document.getContentFileName(), getOutputFormat()));
            }
            final byte[] newContent = converter.convert();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Conversion done in %s ms", System.currentTimeMillis() - time));
            }
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
        return processAPI.getLastDocument(processInstanceId, getSourceDocumentReference());
    }

    private String getSourceDocumentReference() {
        return (String) getInputParameter(SOURCE_DOCUMENT);
    }

    private String getOutputFormat() {
        return (String) getInputParameter(OUTPUT_FORMAT, ConverterTypeTo.PDF.name());
    }

    private String getEncoding() {
        return (String) getInputParameter(ENCODING, DEFAULT_ENCODING);
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
        inputParameters.put(ENCODING, getInputParameter(ENCODING));
        return inputParameters;
    }

}
