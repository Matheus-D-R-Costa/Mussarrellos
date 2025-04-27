package com.mussarrellos.backend.modules.customer.infra.persistance.repository;

import com.mussarrellos.backend.modules.customer.infra.persistance.model.CustomerModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomerModelRepository extends ReactiveCrudRepository<CustomerModel, UUID> {

    @Query("SELECT * FROM clients WHERE LOWER(email) = LOWER(:email)")
    Mono<CustomerModel> findByEmail(String email);

    @Query("SELECT COUNT(*) > 0 FROM clients WHERE LOWER(email) = LOWER(:email)")
    Mono<Boolean> existsByEmail(String email);
}
