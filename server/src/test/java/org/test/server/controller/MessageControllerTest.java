package org.test.server.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.test.server.ServerApplication;
import org.test.server.entity.Message;
import org.test.server.entity.MessageDto;
import org.test.server.entity.TimeBetweenDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ServerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({ "classpath:schema.sql", "classpath:data.sql" })
class MessageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public String getWayToApi(final String point) {
        return "http://localhost:" + port + "/api/v1/" + "/messages/" + point;
    }

    @Test @DisplayName("Записываем сообщение на сервер - receivingMessages")
    void receivingMessages() {
        MessageDto message = new MessageDto();
        message.setText("TEST_SEND");
        message.setDateTime(LocalDateTime.now());

        assertTrue(
                restTemplate.postForEntity(getWayToApi(""), message, Object.class)
                        .getStatusCode().is2xxSuccessful()
        );
    }

    @Test @DisplayName("Берем сообщения с сервера по Id - readMessageById")
    void readMessageById() {
        assertEquals(
                restTemplate.getForObject(getWayToApi("1"), Message.class)
                        .getId(), 1L
        );
    }

    @Test @DisplayName("Берем сообщения за период - readMessagesInRangesTime")
    void readMessagesInRangesTime() {
        assertTrue(
        Objects.requireNonNull(restTemplate.postForEntity(getWayToApi("between-time"),
                new TimeBetweenDto(LocalDateTime.parse("2022-01-23T22:05:30"), LocalDateTime.parse("2022-03-23T22:05:40")),
                List.class).getBody()).size() >= 3 // В тестовых данных 3 записи соответствующие запросу, проверяем, что они вернутся
        );
    }
}