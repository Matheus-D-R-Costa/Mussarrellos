package com.mussarrellos.backend.modules.client.domain.repository;

import com.mussarrellos.backend.modules.client.domain.entity.Client;
import com.mussarrellos.backend.modules.client.domain.entity.types.ClientId;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * ‘Interface’ que define as operações de persistência para entidades Client.
 * Seguindo o padrão Repository do DDD, representa a porta de saída do domínio para a infraestrutura.
 */
public interface ClientRepository {
    /**
     * Salva um cliente no repositório.
     *
     * @param client Cliente a ser salvo
     * @return ID do cliente salvo
     */
    Mono<UUID> save(Client client);
    
    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente
     * @return Cliente encontrado ou vazio
     */
    Mono<Client> findById(ClientId id);
    
    /**
     * Busca um cliente pelo email.
     *
     * @param email Email do cliente
     * @return Cliente encontrado ou vazio
     */
    Mono<Client> findByEmail(String email);
    
    /**
     * Verifica se um email é único no sistema.
     *
     * @param email Email a ser verificado
     * @return true se for único, false caso contrário
     */
    Mono<Boolean> checkEmailUniqueness(String email);
} 