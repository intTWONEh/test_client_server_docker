package org.test.client.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Data
public class TimeBetweenDto {
    private LocalDateTime timeStart;
    private LocalDateTime endTime;

   public TimeBetweenDto(LocalDateTime timeStart, LocalDateTime endTime) {
        if(timeStart.isBefore(endTime)) {
            this.timeStart = timeStart;
            this.endTime = endTime;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be before end date!");
        }
    }
}
