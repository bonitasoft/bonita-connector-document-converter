/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bonitasoft.engine.connector.ConnectorValidationException;

public class InputParametersValidator {

    private final Map<String, Object> inputParameters;

    public InputParametersValidator(final Map<String, Object> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public void validateInputParameters() throws ConnectorValidationException {
        validateSourceDocumentInput();
        validateOutputFileName();
        validateEncoding();
    }

    private void validateSourceDocumentInput() throws ConnectorValidationException {
        final Object sourceDocumentName = inputParameters.get(DocumentConverterConnector.SOURCE_DOCUMENT);
        if (sourceDocumentName == null) {
            throw new ConnectorValidationException(String.format("Input parameter %s cannot be null.", DocumentConverterConnector.SOURCE_DOCUMENT));
        }
        if (!(sourceDocumentName instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input parameter %s must be of type %s.", DocumentConverterConnector.SOURCE_DOCUMENT, String.class.getName()));
        }
        if (((String) sourceDocumentName).isEmpty()) {
            throw new ConnectorValidationException(String.format("Input parameter %s cannot be empty.", DocumentConverterConnector.SOURCE_DOCUMENT));
        }
    }

    private void validateOutputFileName() throws ConnectorValidationException {
        final Object outputFileName = inputParameters.get(DocumentConverterConnector.OUTPUT_FILE_NAME);
        if (outputFileName != null && !(outputFileName instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input parameter %s must be of type %s.", DocumentConverterConnector.OUTPUT_FILE_NAME, String.class.getName()));
        }
        if (outputFileName != null && !isValidFileName((String) outputFileName)) {
            throw new ConnectorValidationException(
                    String.format("Input parameter %s has an invalid value: %s. You must specify a valid file name.",
                            DocumentConverterConnector.OUTPUT_FILE_NAME, outputFileName));
        }
    }

    private boolean isValidFileName(final String outputFileName) {
        final File f = new File(outputFileName);
        try {
            f.getCanonicalPath();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }


    private void validateEncoding() throws ConnectorValidationException {
        final Object encoding = inputParameters.get(DocumentConverterConnector.ENCODING);
        if (encoding != null && !(encoding instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input parameter %s must be of type %s.", DocumentConverterConnector.OUTPUT_FILE_NAME, String.class.getName()));
        }
        if (encoding != null && !java.nio.charset.Charset.isSupported((String) encoding)) {
            throw new ConnectorValidationException(
                    String.format("Input parameter %s has an invalid value: %s is not a supported encoding.",
                            DocumentConverterConnector.ENCODING, encoding));
        }
    }


}
