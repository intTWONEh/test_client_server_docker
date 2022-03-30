package org.test.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.test.server.entity.Message;
import org.test.server.entity.MessageDto;
import org.test.server.entity.TimeBetweenDto;
import org.test.server.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages/")
    public void receivingMessages(@RequestBody MessageDto message) {
        messageService.writeMessage(message);
    }

    @GetMapping("/messages/{id}")
    public Message readMessageById(@PathVariable Long id) {
        return messageService.readMessageById(id);
    }

    @PostMapping("/messages/between-time")
    public List<Message> readMessagesInRangesTime(@RequestBody TimeBetweenDto timeBetweenDto) {
        return messageService.readMessagesInRangesTime(timeBetweenDto.getTimeStart(), timeBetweenDto.getEndTime());
    }

}