package com.mussarrellos.backend.modules.customer.domain.repository;

import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
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
     * @param customer Cliente a ser salvo
     * @return ID do cliente salvo
     */
    Mono<UUID> save(Customer customer);
    
    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente
     * @return Cliente encontrado ou vazio
     */
    Mono<Customer> findById(CustomerId id);
    
    /**
     * Busca um cliente pelo email.
     *
     * @param email Email do cliente
     * @return Cliente encontrado ou vazio
     */
    Mono<Customer> findByEmail(String email);
    
    /**
     * Verifica se um email é único no sistema.
     *
     * @param email Email a ser verificado
     * @return true se for único, false caso contrário
     */
    Mono<Boolean> checkEmailUniqueness(String email);
} 