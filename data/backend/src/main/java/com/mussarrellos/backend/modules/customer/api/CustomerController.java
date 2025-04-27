package com.mussarrellos.backend.modules.customer.api;

import com.mussarrellos.backend.buildingblocks.application.mediator.Mediator;
import com.mussarrellos.backend.modules.customer.api.requests.ChangeCustomerEmailRequest;
import com.mussarrellos.backend.modules.customer.api.requests.ChangeCustomerPasswordRequest;
import com.mussarrellos.backend.modules.customer.api.requests.RegisterCustomerRequest;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeCustomerEmailCommand;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeCustomerPasswordCommand;
import com.mussarrellos.backend.modules.customer.application.commands.RegisterCustomerCommand;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;
import com.mussarrellos.backend.modules.customer.application.queries.GetCustomerByEmailQuery;
import com.mussarrellos.backend.modules.customer.application.queries.GetCustomerByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/mussarellos/clients")
@RequiredArgsConstructor
public class CustomerController {

    private final Mediator mediator;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> registerClient(@RequestBody RegisterCustomerRequest request) {
        log.info("Registrando novo cliente com email: {}", request.email());
        return mediator.send(
                new RegisterCustomerCommand(request.email(), request.password())
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CustomerDto>> getClientById(@PathVariable UUID id) {
        log.info("Buscando cliente por ID: {}", id);
        return mediator.send(new GetCustomerByIdQuery(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CustomerDto>> getClientByEmail(@RequestParam String email) {
        log.info("Buscando cliente por email: {}", email);
        return mediator.send(new GetCustomerByEmailQuery(email))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{id}/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientEmail(@PathVariable UUID id, @RequestBody ChangeCustomerEmailRequest request) {
        log.info("Alterando email do cliente ID: {}", id);
        return mediator.send(
                new ChangeCustomerEmailCommand(id, request.newEmail())
        );
    }

    @PatchMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientPassword(@PathVariable UUID id, @RequestBody ChangeCustomerPasswordRequest request) {
        log.info("Alterando senha do cliente ID: {}", id);
        return mediator.send(
                new ChangeCustomerPasswordCommand(id, request.getCurrentPassword(), request.getNewPassword())
        );
    }
} 