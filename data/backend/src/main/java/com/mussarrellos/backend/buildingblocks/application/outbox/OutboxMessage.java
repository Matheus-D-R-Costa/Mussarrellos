package com.mussarrellos.backend.buildingblocks.application.outbox;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED) // Construtor protegido para JPA/frameworks ORM
public class OutboxMessage {

    private UUID id;
    private LocalDateTime occurredOn;
    private String type;
    private String data;
    private LocalDateTime processedDate;

    public OutboxMessage(UUID id, LocalDateTime occurredOn, String type, String data) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.type = type;
        this.data = data;
    }

    public void markAsProcessed() {
        this.processedDate = LocalDateTime.now();
    }

    public boolean isProcessed() {
        return processedDate != null;
    }
} 