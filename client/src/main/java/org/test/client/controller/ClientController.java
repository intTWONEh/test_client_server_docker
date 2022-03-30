package org.test.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.test.client.entity.Message;
import org.test.client.entity.MessageDto;
import org.test.client.entity.TimeBetweenDto;
import org.test.client.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/messages/")
    public void sendMessages(@RequestBody MessageDto message) {
        clientService.sendMessage(message);
    }

    @GetMapping("/messages/{id}")
    public Message getMessageById(@PathVariable Long id) {
        return clientService.getMessageById(id);
    }

    @PostMapping("/messages/between-time")
    public List<Message> readMessagesInRangesTime(@RequestBody TimeBetweenDto timeBetweenDto) {
        return clientService.getMessagesInRangesTime(timeBetweenDto);
    }

    @PostMapping("/connect")
    public void connectToServer(@RequestBody String ip) {
        clientService.connectToServer(ip, 8080);
    }
}
