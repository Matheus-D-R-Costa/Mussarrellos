package com.mussarrellos.backend.buildingblocks.application.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mussarrellos.backend.buildingblocks.DomainEventPublisher;
import com.mussarrellos.backend.buildingblocks.IDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Serviço responsável por processar as mensagens da outbox.
 * Periodicamente busca mensagens não processadas, publica-as e marca como processadas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessorService {
    private final DatabaseClient databaseClient;
    private final DomainEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    /**
     * Processa as mensagens não processadas da outbox a cada 5 segundos.
     * Este método é executado automaticamente pelo Spring Scheduler.
     */
    @Scheduled(fixedDelayString = "${outbox.polling-interval-ms:5000}")
    public void processOutboxMessages() {
        log.debug("Starting to process outbox messages");

        databaseClient.sql(
                        "SELECT id, occurred_on, type, data FROM outbox_messages " +
                                "WHERE processed_date IS NULL " +
                                "ORDER BY occurred_on ASC " +
                                "LIMIT 100")
                .map((row, metadata) -> {
                    UUID id = row.get("id", UUID.class);
                    LocalDateTime occurredOn = row.get("occurred_on", LocalDateTime.class);
                    String type = row.get("type", String.class);
                    String data = row.get("data", String.class);

                    return new OutboxMessage(id, occurredOn, type, data);
                })
                .all()
                .flatMap(this::processMessage)
                .subscribe(
                        null,
                        error -> log.error("Error processing outbox messages", error),
                        () -> log.debug("Finished processing outbox messages")
                );
    }

    /**
     * Processa uma única mensagem da outbox.
     * Tenta converter a mensagem em um evento de domínio, publicá-lo e marcar como processado.
     *
     * @param message A mensagem a ser processada
     * @return Um Mono que completa quando a operação for bem-sucedida
     */
    @Transactional
    public Mono<Void> processMessage(OutboxMessage message) {
        return Mono.fromCallable(() -> {
                    try {
                        // Carrega a classe do evento dinamicamente
                        Class<?> eventType = Class.forName(message.getType());
                        // Desserializa o evento
                        Object event = objectMapper.readValue(message.getData(), eventType);
                        // Publica o evento
                        eventPublisher.publish((IDomainEvent) event);
                        log.debug("Published event of type {} with ID {}", message.getType(), message.getId());
                        return true;
                    } catch (Exception e) {
                        log.error("Error processing outbox message {}", message.getId(), e);
                        return false;
                    }
                })
                .flatMap(published -> {
                    if (published) {
                        // Marca a mensagem como processada
                        return databaseClient.sql(
                                        "UPDATE outbox_messages SET processed_date = :processedDate " +
                                                "WHERE id = :id")
                                .bind("processedDate", LocalDateTime.now())
                                .bind("id", message.getId())
                                .fetch()
                                .rowsUpdated()
                                .then();
                    } else {
                        return Mono.empty();
                    }
                });
    }
} 