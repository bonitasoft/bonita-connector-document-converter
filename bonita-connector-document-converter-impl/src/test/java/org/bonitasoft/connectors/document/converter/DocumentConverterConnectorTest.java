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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.document.converter.core.DocumentConverter;
import org.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;
import org.bonitasoft.engine.api.APIAccessor;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.bpm.document.impl.DocumentImpl;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.EngineExecutionContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.opensagres.xdocreport.core.XDocReportException;

@RunWith(MockitoJUnitRunner.class)
public class DocumentConverterConnectorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final long processInstanceId = 4861356546l;

    @Mock
    private APIAccessor apiAccessor;
    @Mock
    private EngineExecutionContext engineExecutionContext;
    @Mock
    private ProcessAPI processAPI;
    @Mock
    private DocumentConverterFactory documentConverterFactory;
    @Mock
    private DocumentConverter converter;

    private DocumentConverterConnector connector;

    @Before
    public void before() {
        connector = spy(new DocumentConverterConnector(documentConverterFactory));
        doReturn(apiAccessor).when(connector).getAPIAccessor();
        doReturn(engineExecutionContext).when(connector).getExecutionContext();
        doReturn(processAPI).when(apiAccessor).getProcessAPI();
        doReturn(processInstanceId).when(engineExecutionContext).getProcessInstanceId();
    }

    @Test
    public void should_retrieve_document_from_sourceDocument_parameter() throws Exception {
        //given
        final DocumentImpl document = new DocumentImpl();
        document.setContentMimeType("theMimeType");
        document.setFileName("doc.docx");
        document.setContentStorageId("TheStorageID");
        final byte[] content = new byte[] { 4, 5, 6 };
        final byte[] contentAfter = { 1, 2, 3 };
        when(documentConverterFactory.newConverter(any(ByteArrayInputStream.class), anyString(), anyString())).thenReturn(converter);
        when(converter.convert()).thenReturn(contentAfter);
        doReturn(document).when(processAPI).getLastDocument(processInstanceId, "documentName");
        doReturn(content).when(processAPI).getDocumentContent("TheStorageID");

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");

        connector.setInputParameters(parameters);
        connector.validateInputParameters();

        //when
        final Map<String, Object> execute = connector.execute();

        //then
        assertThat(execute).containsOnlyKeys(DocumentConverterConnector.OUTPUT_DOCUMENT_VALUE);
        assertThat(execute.get(DocumentConverterConnector.OUTPUT_DOCUMENT_VALUE))
                .isEqualToComparingFieldByField(new DocumentValue(contentAfter, "application/pdf", "doc.pdf"));
    }

    @Test
    public void should_throw_a_ConnectorException_when_convertion_failed() throws Exception {
        //given
        final DocumentImpl document = new DocumentImpl();
        document.setContentMimeType("theMimeType");
        document.setFileName("doc.docx");
        document.setContentStorageId("TheStorageID");
        final byte[] content = new byte[] { 4, 5, 6 };
        final byte[] contentAfter = { 1, 2, 3 };
        when(documentConverterFactory.newConverter(any(ByteArrayInputStream.class), anyString(), anyString())).thenReturn(converter);
        when(converter.convert()).thenReturn(contentAfter);
        doReturn(document).when(processAPI).getLastDocument(processInstanceId, "documentName");
        doReturn(content).when(processAPI).getDocumentContent("TheStorageID");

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");

        connector.setInputParameters(parameters);
        connector.validateInputParameters();

        doThrow(new XDocReportException("")).when(converter).convert();

        expectedException.expect(ConnectorException.class);

        //when
        connector.execute();
    }

    @Test
    public void should_throw_a_ConnectorException_when_document_not_found() throws Exception {
        //given
        doThrow(new DocumentNotFoundException("")).when(processAPI).getLastDocument(anyLong(), anyString());
        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");
        connector.setInputParameters(parameters);

        expectedException.expect(ConnectorException.class);

        //when
        connector.execute();
    }

}
