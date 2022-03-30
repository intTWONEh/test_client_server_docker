package org.test.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.server.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findByDateTimeBetween(LocalDateTime a, LocalDateTime b);
}
