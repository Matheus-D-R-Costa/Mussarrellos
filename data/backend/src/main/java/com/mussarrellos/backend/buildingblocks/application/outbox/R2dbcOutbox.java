package com.mussarrellos.backend.buildingblocks.application.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface Outbox usando R2DBC para armazenamento reativo.
 * Esta implementação é específica para uso com banco de dados reativos.
 */
@Component
@RequiredArgsConstructor
public class R2dbcOutbox implements Outbox {
    private final DatabaseClient databaseClient;
    private final List<OutboxMessage> messages = new ArrayList<>();

    @Override
    public void add(OutboxMessage message) {
        messages.add(message);
    }

    @Override
    public Mono<Void> save() {
        if (messages.isEmpty()) {
            return Mono.empty();
        }

        // Cria um batch de operações para inserir todas as mensagens
        List<OutboxMessage> messagesToSave = new ArrayList<>(messages);
        messages.clear();

        // Usa DatabaseClient para inserir as mensagens na tabela outbox_messages
        return Mono.when(
                messagesToSave.stream()
                        .map(message -> databaseClient.sql(
                                "INSERT INTO outbox_messages (id, occurred_on, type, data, processed_date) " +
                                "VALUES (:id, :occurredOn, :type, :data, :processedDate)")
                                .bind("id", message.getId())
                                .bind("occurredOn", message.getOccurredOn())
                                .bind("type", message.getType())
                                .bind("data", message.getData())
                                .bindNull("processedDate", java.time.LocalDateTime.class)
                                .fetch()
                                .rowsUpdated())
                        .toList()
        ).then();
    }
} 