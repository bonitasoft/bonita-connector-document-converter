package org.bonitasoft.connectors.document.converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.connector.ConnectorValidationException;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;

public class InputParametersValidator {

    static final List<String> SUPPORTED_FORMATS = new ArrayList<String>();

    static {
        SUPPORTED_FORMATS.add(ConverterTypeTo.PDF.name());
        SUPPORTED_FORMATS.add(ConverterTypeTo.XHTML.name());
    }

    private final Map<String, Object> inputParameters;

    public InputParametersValidator(final Map<String, Object> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public void validateInputParameters() throws ConnectorValidationException {
        validateSourceDocumentInput();
        validateOutputFormatInput();
        validateOutputFileName();
        validateEncoding();
    }

    private void validateSourceDocumentInput() throws ConnectorValidationException {
        final Object sourceDocumentName = inputParameters.get(DocumentConverterConnector.SOURCE_DOCUMENT);
        if (sourceDocumentName == null) {
            throw new ConnectorValidationException(String.format("Input paramater %s cannot be null.", DocumentConverterConnector.SOURCE_DOCUMENT));
        }
        if (!(sourceDocumentName instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s must be of type %s.", DocumentConverterConnector.SOURCE_DOCUMENT, String.class.getName()));
        }
        if (((String) sourceDocumentName).isEmpty()) {
            throw new ConnectorValidationException(String.format("Input paramater %s cannot be empty.", DocumentConverterConnector.SOURCE_DOCUMENT));
        }
    }

    private void validateOutputFileName() throws ConnectorValidationException {
        final Object outputFileName = inputParameters.get(DocumentConverterConnector.OUTPUT_FILE_NAME);
        if (outputFileName != null && !(outputFileName instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s must be of type %s.", DocumentConverterConnector.OUTPUT_FILE_NAME, String.class.getName()));
        }
        if (outputFileName != null && !validFileName((String) outputFileName)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s has an invalid value: %s. You must specify a valid file name.",
                            DocumentConverterConnector.OUTPUT_FILE_NAME, outputFileName));
        }
    }

    private boolean validFileName(final String outputFileName) {
        final File f = new File(outputFileName);
        try {
            f.getCanonicalPath();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    private void validateOutputFormatInput() throws ConnectorValidationException {
        final Object outputFormat = inputParameters.get(DocumentConverterConnector.OUTPUT_FORMAT);
        if (outputFormat != null && !(outputFormat instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s must be of type %s.", DocumentConverterConnector.OUTPUT_FORMAT, String.class.getName()));
        }
        if (outputFormat != null && !acceptFormat((String) outputFormat)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s has an invalid value: %s. Allowed values are %s", DocumentConverterConnector.OUTPUT_FORMAT, outputFormat,
                            SUPPORTED_FORMATS));
        }
    }

    private void validateEncoding() throws ConnectorValidationException {
        final Object encoding = inputParameters.get(DocumentConverterConnector.ENCODING);
        if (encoding != null && !(encoding instanceof String)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s must be of type %s.", DocumentConverterConnector.OUTPUT_FILE_NAME, String.class.getName()));
        }
        if (encoding != null && !java.nio.charset.Charset.isSupported((String) encoding)) {
            throw new ConnectorValidationException(
                    String.format("Input paramater %s has an invalid value: %s is not a supported encoding.",
                            DocumentConverterConnector.ENCODING, encoding));
        }
    }

    private boolean acceptFormat(final String outputFormat) {
        return SUPPORTED_FORMATS.contains(outputFormat);
    }

}
