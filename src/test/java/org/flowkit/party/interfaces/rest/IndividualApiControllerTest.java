package org.flowkit.party.interfaces.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.flowkit.party.application.commandservices.IndividualCommandService;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.commands.CreateIndividualCommandResult;
import org.flowkit.party.domain.model.valueobjects.Version;
import org.flowkit.party.interfaces.rest.openapi.dto.CreateIndividualResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IndividualApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndividualCommandService commandService;

    private final String basePath = "/tmf-api/party/v4/individual";

    @Test
    @DisplayName("POST /individual/ -  Minimum required attributes")
    @SneakyThrows
    void testCreateIndividualMinimum() {
        CreateIndividualResource individualResource = getIndividualResource();

        doReturn(mockCommandResultSuccess()).when(commandService).createIndividual(any());

        ResultActions result = mockMvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(individualResource)));

        String id = getGeneratedIdFromResult(result);

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"" + Version.initialVersion().getVersion().toString() + "\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost" + basePath + "/" + id))
                .andExpect(jsonPath("$.familyName", is("Lamborgizzia")))
                .andExpect(jsonPath("$.givenName", is("Jane")));
    }


    @Test
    @DisplayName("POST /individual/ -  Fail with error - Individual already exists")
    @SneakyThrows
    void testCreateIndividualWhenAlreadyExists() {
        CreateIndividualResource individualResource = getIndividualResource();

        doReturn(mockCommandResultFailure()).when(commandService).createIndividual(any());

        mockMvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(individualResource)))

                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(header().doesNotExist(HttpHeaders.ETAG))
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.title", is("Conflict")))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.detail", is("Individual already exists")));
    }

    private CreateIndividualResource getIndividualResource() {
        CreateIndividualResource individualResource = new CreateIndividualResource();
        individualResource.setFamilyName("Lamborgizzia");
        individualResource.setGivenName("Jane");
        return individualResource;
    }

    private CreateIndividualCommandResult mockCommandResultSuccess() {

        CreateIndividualCommand command = new CreateIndividualCommand();
        command.setFamilyName("Lamborgizzia");
        command.setGivenName("Jane");
        var individual = new org.flowkit.party.domain.model.aggregates.Individual(command);
        return new CreateIndividualCommandResult(individual);
    }

    private CreateIndividualCommandResult mockCommandResultFailure() {
        List<String> failureReasons = new ArrayList<>();
        failureReasons.add("Individual already exists");
        return new CreateIndividualCommandResult(failureReasons);
    }

    private String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private org.flowkit.party.interfaces.rest.openapi.dto.Individual asIndividualDTOObject(final String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, org.flowkit.party.interfaces.rest.openapi.dto.Individual.class);
    }

    private String getGeneratedIdFromResult(ResultActions result) throws JsonProcessingException, UnsupportedEncodingException {
        var individual = asIndividualDTOObject(result.andReturn().getResponse().getContentAsString());
        return individual.getId();
    }
}