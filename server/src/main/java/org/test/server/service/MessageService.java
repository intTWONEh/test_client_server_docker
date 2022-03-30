package org.test.server.service;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.test.server.entity.Message;
import org.test.server.entity.MessageDto;
import org.test.server.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public void writeMessage(@NotNull final MessageDto message) {
        messageRepository.save(message.toMessage());
    }

    public Message readMessageById(@NotNull final Long id) {
        return messageRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found for this id!")
        );
    }

    public List<Message> readMessagesInRangesTime(@NotNull final LocalDateTime startTime, @NotNull final LocalDateTime endTime) {
        return messageRepository.findByDateTimeBetween(startTime, endTime).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found in given time range!")
        );
    }
}