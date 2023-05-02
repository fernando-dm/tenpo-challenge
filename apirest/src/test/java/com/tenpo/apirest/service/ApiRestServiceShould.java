package com.tenpo.apirest.service;

import com.tenpo.apirest.domain.History;
import com.tenpo.apirest.dto.HistoryDto;
import com.tenpo.apirest.exceptions.ApplyPercentageException;
import com.tenpo.apirest.exceptions.HistoryNotFoundException;
import com.tenpo.apirest.repository.ApiRestRepository;
import com.tenpo.apirest.utils.RetryMethod;
import com.tenpo.clients.share.ShareClient;
import com.tenpo.clients.share.ShareResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;

import static com.tenpo.apirest.utils.RetryMethod.retryMethod;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ApiRestServiceShould {

    @Mock
    private ApiRestRepository apiRestRepository;

    @Mock
    private ShareClient shareClient;

    @InjectMocks
    private ApiRestService apiRestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testApplyPercentage() throws ApplyPercentageException {
        // given
        ShareResponse mockedResponse = new ShareResponse(10.0);
        when(shareClient.getShare()).thenReturn(mockedResponse);

        // when
        double result = apiRestService.applyPercentage(1.0, 2.0).shareValue();

        // then
        double expected = 13.0; // (1.0 + 2.0) + 10.0
        assertEquals(expected, result, 0.0);
    }

    @Test
    void testGetShareFromCache() throws ApplyPercentageException {
        //given
        ShareResponse mockedResponse = new ShareResponse(10.0);
        when(shareClient.getShare()).thenReturn(mockedResponse);

        // when
        ShareResponse result = apiRestService.getShareFromCache();

        // then
        assertNotNull(result);
        assertEquals(mockedResponse.shareValue(), result.shareValue(), 0.0);
    }

    @Test
    void testCleanCache() {
        // call the service method
        apiRestService.cleanCache();

        // verify that the method was called on the repository mock has not int
        verifyNoMoreInteractions(apiRestRepository);
    }

    @Test
    void testSaveHistory() throws ApplyPercentageException {
        // given
        ShareResponse mockedResponse = new ShareResponse(10.0);
        when(shareClient.getShare()).thenReturn(mockedResponse);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getScheme()).thenReturn("http");
        when(mockRequest.getServerName()).thenReturn("localhost");
        when(mockRequest.getServerPort()).thenReturn(8080);
        when(mockRequest.getRequestURI()).thenReturn("/api");
        when(mockRequest.getParameterMap())
                .thenReturn(
                        Collections.singletonMap("number1", new String[] {"number2"}));

        // when
        apiRestService.saveHistory(13.0, mockRequest);

        // then
        verify(apiRestRepository).save(any(History.class));
    }

    @Test
    void testGetHistory() throws HistoryNotFoundException {
        // given
        History mockHistory = new History(1L, "http://localhost:8080/api?number1=5, number2=5", "11.0", "OK");
        when(apiRestRepository.findHistory()).thenReturn(Collections.singletonList(mockHistory));

        // when
        List<HistoryDto> result = apiRestService.getHistory();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockHistory.getId(), result.get(0).id());
        assertEquals(mockHistory.getEndpointUrl(), result.get(0).endpointUrl());
        assertEquals(mockHistory.getResponse(), result.get(0).response());
        assertEquals(mockHistory.getStatus(), result.get(0).status());
    }




}
