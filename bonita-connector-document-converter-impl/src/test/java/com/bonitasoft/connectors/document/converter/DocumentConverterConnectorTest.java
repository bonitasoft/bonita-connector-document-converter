/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.bonitasoft.connectors.document.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

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

import com.bonitasoft.connectors.document.converter.core.DocumentConverter;
import com.bonitasoft.connectors.document.converter.core.DocumentConverterFactory;
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
