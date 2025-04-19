package com.mussarrellos.backend.modules.user.infra.persistance.repository;

import com.mussarrellos.backend.modules.user.infra.persistance.model.ClientModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repositório para operações relacionadas à persistência de clientes.
 * Utiliza R2DBC para acesso reativo ao banco de dados.
 */
@Repository
public interface ClientModelRepository extends ReactiveCrudRepository<ClientModel, UUID> {

    /**
     * Busca um cliente pelo email.
     *
     * @param email Email do cliente
     * @return Cliente encontrado ou vazio
     */
    @Query("SELECT * FROM clients WHERE LOWER(email) = LOWER(:email)")
    Mono<ClientModel> findByEmail(String email);
    
    /**
     * Verifica se existe um cliente com o email informado.
     *
     * @param email Email a ser verificado
     * @return true se existir, false caso contrário
     */
    @Query("SELECT COUNT(*) > 0 FROM clients WHERE LOWER(email) = LOWER(:email)")
    Mono<Boolean> existsByEmail(String email);
}
