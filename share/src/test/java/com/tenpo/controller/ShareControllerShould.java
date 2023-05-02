package com.tenpo.controller;

import com.tenpo.clients.share.ShareResponse;
import com.tenpo.share.controller.ShareController;
import com.tenpo.share.services.ShareService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ShareController.class, ShareService.class})
@AutoConfigureMockMvc
public class ShareControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShareService shareService;

    @Test
    public void testGetShareReturnsShareResponse() throws Exception {
        // given
        Double share = 25.0;
        Mockito.when(shareService.getShare()).thenReturn(share);

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/share")
                        .accept(MediaType.APPLICATION_JSON)) // set Accept header
                .andReturn();

        //then
        assertNotNull(result);
    }

}
