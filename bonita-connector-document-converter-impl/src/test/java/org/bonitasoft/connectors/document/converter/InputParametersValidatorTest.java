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

import static org.assertj.core.api.Assertions.entry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.assertj.core.data.MapEntry;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InputParametersValidatorTest {

    @Rule
    public ExpectedException excpetionRule = ExpectedException.none();

    @Test
    public void should_throw_a_ConnectorValidationException_if_sourceDocument_is_null() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap());

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_sourceDocument_is_not_a_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", 0)));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_sourceDocument_is_an_empty_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "")));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_outputFormat_is_set_and_not_a_valid_string_value() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "aDocumentName"), entry("outputFormat", "PPT")));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_outputFormat_is_set_and_not_a_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "aDocumentName"), entry("outputFormat", 0)));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_outputFileName_is_set_and_not_a_String() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(aMap(entry("sourceDocument", "aDocumentName"), entry("outputFileName", 0)));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_outputFileName_is_set_and_not_a_valid_filename() throws Exception {
        Assume.assumeTrue(SystemUtils.IS_OS_WINDOWS);
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("outputFileName", "aInvalidFileName?")));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    @Test
    public void should_be_valid_if_optional_parameters_are_not_set() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName")));

        validator.validateInputParameters();
    }

    @Test
    public void should_validate_input_parameters() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("outputFormat", "PDF"), entry("outputFileName", "aValidaFileName.pdf")));

        validator.validateInputParameters();
    }

    @Test
    public void should_throw_a_ConnectorValidationException_if_encoding_is_set_and_not_valid() throws Exception {
        final InputParametersValidator validator = new InputParametersValidator(
                aMap(entry("sourceDocument", "aDocumentName"), entry("encoding", "invalid")));

        excpetionRule.expect(ConnectorValidationException.class);
        validator.validateInputParameters();
    }

    private Map<String, Object> aMap(final MapEntry<String, ?>... mapEntries) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final MapEntry<String, ?> mapEntry : mapEntries) {
            parameters.put(mapEntry.key, mapEntry.value);
        }
        return parameters;
    }

}
