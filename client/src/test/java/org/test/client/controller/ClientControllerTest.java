package org.test.client.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.test.client.WireMockInitializer;
import org.test.client.entity.TimeBetweenDto;
import org.test.client.service.ClientService;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
class ClientControllerTest {
    @Autowired private WireMockServer wireMockServer;
    @Autowired private ClientController clientController;
    @Autowired private ClientService clientService;
    @AfterEach void resetAll() {
        wireMockServer.resetAll();
    }

    @BeforeEach
    void init() {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/api/v1/messages/33"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("api-file/responseId.json"))
        );

        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/messages/between-time"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("api-file/responseRangesTime.json"))
        );


        clientService.connectToServer("localhost", wireMockServer.port());
    }


    @Test
    void sendMessages() {
    }

    @Test
    void getMessageById() {
        assertEquals("_TEST_", clientController.getMessageById(33L).getText());
    }

    @Test
    void readMessagesInRangesTime() {
        assertEquals(2, clientController.readMessagesInRangesTime(new TimeBetweenDto(LocalDateTime.parse("2020-03-29T13:05:08.864"), LocalDateTime.parse("2030-03-29T13:05:08.864"))).size());
    }
}