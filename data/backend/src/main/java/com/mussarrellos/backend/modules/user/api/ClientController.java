package com.mussarrellos.backend.modules.user.api;

import com.mussarrellos.backend.modules.user.api.requests.ChangeClientEmailRequest;
import com.mussarrellos.backend.modules.user.api.requests.ChangeClientPasswordRequest;
import com.mussarrellos.backend.modules.user.api.requests.RegisterClientRequest;
import com.mussarrellos.backend.modules.user.application.commands.ChangeClientEmailCommand;
import com.mussarrellos.backend.modules.user.application.commands.ChangeClientPasswordCommand;
import com.mussarrellos.backend.modules.user.application.commands.RegisterClientCommand;
import com.mussarrellos.backend.modules.user.application.contracts.IUserModule;
import com.mussarrellos.backend.modules.user.application.dtos.ClientDto;
import com.mussarrellos.backend.modules.user.application.queries.GetClientByEmailQuery;
import com.mussarrellos.backend.modules.user.application.queries.GetClientByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Controller para operações relacionadas a clientes.
 * 
 * Este controller implementa a abordagem recomendada de utilizar diretamente
 * o IUserModule para executar comandos e consultas, seguindo o padrão Mediator
 * para centralizar a execução das operações do módulo.
 */
@RestController
@RequestMapping("/mussarellos/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IUserModule userModule;

    /**
     * Registra um novo cliente.
     *
     * @param request Dados para registro do cliente
     * @return ID do cliente registrado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> registerClient(@RequestBody RegisterClientRequest request) {
        return userModule.executeCommand(
            new RegisterClientCommand(request.getEmail(), request.getPassword())
        );
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente
     * @return Dados do cliente
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClientDto>> getClientById(@PathVariable UUID id) {
        return userModule.executeQuery(new GetClientByIdQuery(id))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Busca um cliente pelo email.
     *
     * @param email Email do cliente
     * @return Dados do cliente
     */
    @GetMapping("/by-email")
    public Mono<ResponseEntity<ClientDto>> getClientByEmail(@RequestParam String email) {
        return userModule.executeQuery(new GetClientByEmailQuery(email))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Altera o email de um cliente.
     *
     * @param id ID do cliente
     * @param request Dados para alteração do email
     * @return Status 204 (No Content)
     */
    @PatchMapping("/{id}/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientEmail(@PathVariable UUID id, @RequestBody ChangeClientEmailRequest request) {
        return userModule.executeCommand(
            new ChangeClientEmailCommand(id, request.newEmail())
        );
    }

    /**
     * Altera a senha de um cliente.
     *
     * @param id ID do cliente
     * @param request Dados para alteração da senha
     * @return Status 204 (No Content)
     */
    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changeClientPassword(@PathVariable UUID id, @RequestBody ChangeClientPasswordRequest request) {
        return userModule.executeCommand(
            new ChangeClientPasswordCommand(id, request.getCurrentPassword(), request.getNewPassword())
        );
    }
} 