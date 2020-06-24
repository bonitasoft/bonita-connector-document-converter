/**
 * Copyright (C) 2020 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.document.converter;

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
