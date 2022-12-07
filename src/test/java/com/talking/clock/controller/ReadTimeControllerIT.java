package com.talking.clock.controller;

import com.talking.clock.shell.ScriptRunner;
import com.talking.clock.service.TimeConversionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {ReadTimeController.class, TimeConversionService.class, ScriptRunner.class})
@WebMvcTest
class ReadTimeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCurretTime() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/timeinwords")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn();

        String currentTime = result.getResponse().getContentAsString();
        assertNotNull(currentTime);
    }

    @Test
    public void testProvidedTime() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/timeinwords/12:12")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn();

        String givenTime = result.getResponse().getContentAsString();
        assertNotNull(givenTime);
        assertEquals("{\"value\":\"twelve past twelve\"}", givenTime);
    }

    @Test
    public void testBadInput() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/timeinwords/qb:12")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String error = result.getResponse().getContentAsString();
        assertNotNull(error);
        assertEquals("{\"error\":\"invalid input.\"}", error);
    }
}