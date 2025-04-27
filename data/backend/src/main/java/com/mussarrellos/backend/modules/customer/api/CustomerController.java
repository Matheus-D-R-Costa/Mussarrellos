package com.mussarrellos.backend.modules.customer.api;

import com.mussarrellos.backend.buildingblocks.application.mediator.Mediator;
import com.mussarrellos.backend.modules.customer.api.requests.ChangeClientEmailRequest;
import com.mussarrellos.backend.modules.customer.api.requests.ChangeClientPasswordRequest;
import com.mussarrellos.backend.modules.customer.api.requests.RegisterClientRequest;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeClientEmailCommand;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeClientPasswordCommand;
import com.mussarrellos.backend.modules.customer.application.commands.RegisterClientCommand;
import com.mussarrellos.backend.modules.customer.application.dtos.ClientDto;
import com.mussarrellos.backend.modules.customer.application.queries.GetClientByEmailQuery;
import com.mussarrellos.backend.modules.customer.application.queries.GetClientByIdQuery;
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
public class ClientController {

    private static final int MAX_KEY_TIMEOUT_MINUTES = 30;

    private final Mediator mediator;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> registerClient(@RequestBody RegisterClientRequest request) {
        log.info("Registrando novo cliente com email: {}", request.email());
        return mediator.send(
                new RegisterClientCommand(request.email(), request.password())
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ClientDto>> getClientById(@PathVariable UUID id) {
        log.info("Buscando cliente por ID: {}", id);
        return mediator.send(new GetClientByIdQuery(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ClientDto>> getClientByEmail(@RequestParam String email) {
        log.info("Buscando cliente por email: {}", email);
        return mediator.send(new GetClientByEmailQuery(email))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{id}/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientEmail(@PathVariable UUID id, @RequestBody ChangeClientEmailRequest request) {
        log.info("Alterando email do cliente ID: {}", id);
        return mediator.send(
                new ChangeClientEmailCommand(id, request.newEmail())
        );
    }

    @PatchMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientPassword(@PathVariable UUID id, @RequestBody ChangeClientPasswordRequest request) {
        log.info("Alterando senha do cliente ID: {}", id);
        return mediator.send(
                new ChangeClientPasswordCommand(id, request.getCurrentPassword(), request.getNewPassword())
        );
    }
} 