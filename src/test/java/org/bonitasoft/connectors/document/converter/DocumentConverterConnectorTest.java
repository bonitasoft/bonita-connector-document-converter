/*
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package org.bonitasoft.connectors.document.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.InputStream;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import fr.opensagres.xdocreport.core.XDocReportException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentConverterConnectorTest {
    
    private final long processInstanceId = 4861356546l;

    @Mock APIAccessor apiAccessor;
    
    @Mock EngineExecutionContext engineExecutionContext;
    
    @Mock ProcessAPI processAPI;

    @Mock DocumentConverterFactory documentConverterFactory;

    @Mock DocumentConverter converter;

    private DocumentConverterConnector connector;

    @BeforeEach
    public void before() {
        when(documentConverterFactory.newConverter(any(InputStream.class), anyString(), anyString())).thenReturn(converter);
        connector = spy(new DocumentConverterConnector(documentConverterFactory));
        doReturn(apiAccessor).when(connector).getAPIAccessor();
        doReturn(engineExecutionContext).when(connector).getExecutionContext();
        doReturn(processAPI).when(apiAccessor).getProcessAPI();
        doReturn(processInstanceId).when(engineExecutionContext).getProcessInstanceId();
    }

    @Test
    void should_retrieve_document_from_sourceDocument_parameter() throws Exception {
        //given
        final DocumentImpl document = new DocumentImpl();
        document.setContentMimeType("theMimeType");
        document.setFileName("doc.docx");
        document.setContentStorageId("TheStorageID");
        final byte[] content = new byte[] { 4, 5, 6 };
        final byte[] contentAfter = { 1, 2, 3 };
        
        when(converter.convert()).thenReturn(contentAfter);
        doReturn(document).when(processAPI).getLastDocument(processInstanceId, "documentName");
        doReturn(content).when(processAPI).getDocumentContent("TheStorageID");

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");
        parameters.put(DocumentConverterConnector.ENCODING, "UTF-8");

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
    void should_throw_a_ConnectorException_when_convertion_failed() throws Exception {
        //given
        final DocumentImpl document = new DocumentImpl();
        document.setContentMimeType("theMimeType");
        document.setFileName("doc.docx");
        document.setContentStorageId("TheStorageID");
        final byte[] content = new byte[] { 4, 5, 6 };
        final byte[] contentAfter = { 1, 2, 3 };
        when(converter.convert()).thenReturn(contentAfter);
        doReturn(document).when(processAPI).getLastDocument(processInstanceId, "documentName");
        doReturn(content).when(processAPI).getDocumentContent("TheStorageID");

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");
        parameters.put(DocumentConverterConnector.ENCODING, "UTF-8");

        connector.setInputParameters(parameters);
        connector.validateInputParameters();

        doThrow(new XDocReportException("")).when(converter).convert();

        //when
        assertThrows(ConnectorException.class, () -> connector.execute());
    }

    @Test
    void should_throw_a_ConnectorException_when_document_not_found() throws Exception {
        //given
        doThrow(new DocumentNotFoundException("")).when(processAPI).getLastDocument(anyLong(), anyString());
        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentConverterConnector.SOURCE_DOCUMENT, "documentName");
        parameters.put(DocumentConverterConnector.ENCODING, "UTF-8");
        connector.setInputParameters(parameters);


        //when
        assertThrows(ConnectorException.class, () -> connector.execute());
    }

}
