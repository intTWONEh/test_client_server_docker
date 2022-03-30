package org.test.server.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private LocalDateTime dateTime;
    private String text;

    public Message toMessage() {
        Message message = new Message();
        message.setText(text);
        message.setDateTime(dateTime);
        return message;
    }
}
