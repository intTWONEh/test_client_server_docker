package org.test.client.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Long id;
    private LocalDateTime dateTime;
    private String text;
}
