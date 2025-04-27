package com.mussarrellos.backend.modules.customer.domain.repository;

import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerRepository {

    Mono<UUID> save(Customer customer);

    Mono<Customer> findById(CustomerId id);

    Mono<Customer> findByEmail(String email);

    Mono<Boolean> checkEmailUniqueness(String email);
} 