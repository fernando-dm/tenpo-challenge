package com.tenpo.apirest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.apirest.dto.HistoryDto;
import com.tenpo.apirest.service.ApiRestService;
import com.tenpo.clients.share.ShareResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiRestControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiRestService apiRestService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetHistoryReturnsListHistoryPaginated() throws Exception {
//        given
        List<HistoryDto> history = new ArrayList<>();
        history.add(new HistoryDto(9L, "http://localhost:8080/api/v1/apirest/apply-percentage/number1=10.5&number2=20.2", "31.614872649808973", "OK"));
        history.add(new HistoryDto(10L, "http://localhost:8080/api/v1/apirest/apply-percentage/number1=10.5&number2=20.2", "31.614872649808973", "OK"));

        Mockito.when(apiRestService.getHistory()).thenReturn(history);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/apirest/history"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<HistoryDto> responseHistory = objectMapper.readValue(responseBody, new TypeReference<List<HistoryDto>>() {
        });

        // then
        assertEquals(history, responseHistory);
    }

    @Test
    public void testApplyPercentageReturnsOk() throws Exception {
        // given
        double number1 = 10.5;
        double number2 = 20.2;
        ShareResponse expectedResponse = new ShareResponse(31.61);

        Mockito.when(apiRestService.applyPercentage(number1, number2)).thenReturn(expectedResponse);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/apirest/apply-percentage")
                        .param("number1", String.valueOf(number1))
                        .param("number2", String.valueOf(number2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ShareResponse actualResponse = objectMapper.readValue(responseBody, ShareResponse.class);

        // then
        assertEquals(expectedResponse, actualResponse);
        Mockito.verify(apiRestService).saveHistory(ArgumentMatchers.eq(expectedResponse.shareValue()), any());
    }

    @Test
    public void testCleanCache() throws Exception {
        // given
        Mockito.doNothing().when(apiRestService).cleanCache();

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/apirest/clean-cache"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        Mockito.verify(apiRestService, Mockito.times(1)).cleanCache();
    }

}
