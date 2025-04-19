package com.mussarrellos.backend.buildingblocks.application.outbox;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma mensagem armazenada na Outbox.
 * Parte do padrão Outbox para garantir consistência entre transações no banco de dados
 * e a publicação de eventos/mensagens em sistemas distribuídos.
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED) // Construtor protegido para JPA/frameworks ORM
public class OutboxMessage {
    private UUID id;
    private LocalDateTime occurredOn;
    private String type;
    private String data;
    private LocalDateTime processedDate;

    /**
     * Cria uma nova mensagem para a Outbox.
     *
     * @param id         Identificador único da mensagem
     * @param occurredOn Data e hora em que o evento ocorreu
     * @param type       Tipo do evento (geralmente o nome completo da classe do evento)
     * @param data       Conteúdo do evento serializado (geralmente JSON)
     */
    public OutboxMessage(UUID id, LocalDateTime occurredOn, String type, String data) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.type = type;
        this.data = data;
    }

    /**
     * Marca a mensagem como processada.
     */
    public void markAsProcessed() {
        this.processedDate = LocalDateTime.now();
    }

    /**
     * Verifica se a mensagem já foi processada.
     *
     * @return true se a mensagem já foi processada, false caso contrário
     */
    public boolean isProcessed() {
        return processedDate != null;
    }
} 