/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package org.bonitasoft.connectors.document.converter;

import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.jupiter.api.Test;

class InputParametersValidatorTest {

    @Test
   void should_throw_a_ConnectorValidationException_if_sourceDocument_is_null() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap());

        assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }

    @Test
   void should_throw_a_ConnectorValidationException_if_sourceDocument_is_not_a_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", 0)));

        assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }

    @Test
   void should_throw_a_ConnectorValidationException_if_sourceDocument_is_an_empty_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "")));

        assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }


    @Test
   void should_throw_a_ConnectorValidationException_if_outputFileName_is_set_and_not_a_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "aDocumentName"), entry("outputFileName", 0)));

        assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }

    @Test
   void should_throw_a_ConnectorValidationException_if_outputFileName_is_set_and_not_a_valid_filename() throws Exception {
        assumeTrue(System.getProperty("os.name").startsWith("Windows"));
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("outputFileName", "aInvalidFileName?")));

        assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }

    @Test
   void should_be_valid_if_optional_parameters_are_not_set() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName")));

        validator.validateInputParameters();
    }

    @Test
   void should_validate_input_parameters() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("outputFormat", "PDF"), entry("outputFileName", "aValidaFileName.pdf")));

        validator.validateInputParameters();
    }

    @Test
   void should_throw_a_ConnectorValidationException_if_encoding_is_set_and_not_valid() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("encoding", "invalid")));

       assertThrows(ConnectorValidationException.class, () -> validator.validateInputParameters());
    }

    private Map<String, Object> aMap(final MapEntry<String, ?>... mapEntries) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final MapEntry<String, ?> mapEntry : mapEntries) {
            parameters.put(mapEntry.key, mapEntry.value);
        }
        return parameters;
    }

}
