package com.mussarrellos.backend.buildingblocks.application.outbox;

import reactor.core.publisher.Mono;

/**
 * Interface para a implementação do padrão Outbox.
 * Permite o armazenamento de mensagens a serem publicadas de forma confiável,
 * garantindo a consistência entre transações de banco de dados e mensageria.
 */
public interface Outbox {
    /**
     * Adiciona uma mensagem à outbox (em memória).
     *
     * @param message A mensagem a ser adicionada
     */
    void add(OutboxMessage message);

    /**
     * Salva todas as mensagens adicionadas à outbox de forma atômica,
     * geralmente como parte da transação principal.
     *
     * @return Um Mono que completa quando a operação for bem-sucedida
     */
    Mono<Void> save();
} 