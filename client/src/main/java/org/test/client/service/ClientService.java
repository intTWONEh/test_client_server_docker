package org.test.client.service;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.test.client.entity.Message;
import org.test.client.entity.MessageDto;
import org.test.client.entity.TimeBetweenDto;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ClientService {
    private WebClient client;

    public void connectToServer(String ip, int port) {
        client = client.mutate().baseUrl("http://" + ip + ":" + port + "/api/v1/").build();
    }

    public ClientService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        client = WebClient.builder()
                .baseUrl("http://localhost:8080/api/v1/")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public void sendMessage(MessageDto message) {
        client.post()
                .uri("/messages/")
                .bodyValue(message)
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "API not found!")))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding!"))
                ).bodyToMono(Void.class).block();
    }


    public Message getMessageById(Long id) {
        return client.get().uri("/messages/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        error -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found for this id!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "API not found!")))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding!")))
                .bodyToMono(Message.class).block();
    }

    public List<Message> getMessagesInRangesTime(TimeBetweenDto timeBetweenDto) {
        return  client.post()
                .uri("/messages/between-time")
                .bodyValue(timeBetweenDto)
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "API not found!")))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding!")))
                .bodyToMono(new ParameterizedTypeReference<List<Message>>() {}).block();
    }
}
