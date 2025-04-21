package com.mussarrellos.backend.buildingblocks.application.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mussarrellos.backend.buildingblocks.DomainEventPublisher;
import com.mussarrellos.backend.buildingblocks.IDomainEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Serviço responsável por processar as mensagens da outbox.
 * Periodicamente busca mensagens não processadas, publica-as e marca como processadas.
 * Implementa padrões de resiliência como Circuit Breaker e Retry.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessorService {
    private final DatabaseClient databaseClient;
    private final DomainEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    
    @Value("${outbox.max-retry-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${outbox.retry-delay-ms:1000}")
    private long retryDelayMs;
    
    @Value("${outbox.batch-size:100}")
    private int batchSize;

    /**
     * Processa as mensagens não processadas da outbox a cada intervalo configurado.
     * Este método é executado automaticamente pelo Spring Scheduler.
     * Implementa Circuit Breaker para evitar sobrecarga quando o sistema está instável.
     */
    @Scheduled(fixedDelayString = "${outbox.polling-interval-ms:5000}")
    @CircuitBreaker(name = "outboxProcessor", fallbackMethod = "fallbackProcessOutboxMessages")
    public void processOutboxMessages() {
        log.debug("Starting to process outbox messages");

        databaseClient.sql(
                "SELECT id, occurred_on, type, data, " +
                "(SELECT COUNT(*) FROM outbox_messages WHERE processed_date IS NULL) as pending_count " +
                "FROM outbox_messages " +
                "WHERE processed_date IS NULL " +
                "ORDER BY occurred_on ASC " +
                "LIMIT :batchSize")
                .bind("batchSize", batchSize)
                .map((row, metadata) -> {
                    UUID id = row.get("id", UUID.class);
                    LocalDateTime occurredOn = row.get("occurred_on", LocalDateTime.class);
                    String type = row.get("type", String.class);
                    String data = row.get("data", String.class);
                    Long pendingCount = row.get("pending_count", Long.class);
                    
                    if (pendingCount != null && pendingCount > 0) {
                        log.debug("Found {} pending outbox messages", pendingCount);
                    }

                    return new OutboxMessage(id, occurredOn, type, data);
                })
                .all()
                .flatMap(this::processMessageWithRetry)
                .subscribe(
                        null,
                        error -> log.error("Error processing outbox messages", error),
                        () -> log.debug("Finished processing outbox messages")
                );
    }
    
    /**
     * Método de fallback para o processamento de mensagens da outbox.
     * Chamado quando o circuit breaker abre o circuito devido a falhas.
     * 
     * @param e A exceção que causou a abertura do circuito
     */
    public void fallbackProcessOutboxMessages(Exception e) {
        log.warn("Circuit breaker ativado para processamento de outbox. Erro: {}", e.getMessage());
        log.debug("Detalhes do erro do circuit breaker:", e);
    }

    /**
     * Processa uma única mensagem da outbox com retentativas.
     * 
     * @param message A mensagem a ser processada
     * @return Um Mono que completa quando a operação for bem-sucedida
     */
    private Mono<Void> processMessageWithRetry(OutboxMessage message) {
        return processMessage(message)
                .retryWhen(Retry.backoff(maxRetryAttempts, Duration.ofMillis(retryDelayMs))
                        .doBeforeRetry(retrySignal -> 
                            log.warn("Retentativa {} de processar mensagem {}: {}",
                                    retrySignal.totalRetries() + 1, 
                                    message.getId(),
                                    retrySignal.failure().getMessage()))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            log.error("Falha após {} tentativas de processar mensagem {}", 
                                    maxRetryAttempts, message.getId(), retrySignal.failure());
                            return retrySignal.failure();
                        }));
    }

    /**
     * Processa uma única mensagem da outbox.
     * Tenta converter a mensagem em um evento de domínio, publicá-lo e marcar como processado.
     *
     * @param message A mensagem a ser processada
     * @return Um Mono que completa quando a operação for bem-sucedida
     */
    @Transactional
    @Retry(name = "outboxProcessor")
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