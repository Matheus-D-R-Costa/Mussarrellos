package com.mussarrellos.backend.modules.authentication.api;

import com.mussarrellos.backend.modules.authentication.api.requests.AuthenticateClientRequest;
import com.mussarrellos.backend.modules.authentication.api.requests.GenerateRSAKeyRequest;
import com.mussarrellos.backend.modules.authentication.application.commands.AuthenticateClientCommand;
import com.mussarrellos.backend.modules.authentication.application.commands.GenerateRSAKeyCommand;
import com.mussarrellos.backend.modules.authentication.application.contracts.IAuthModule;
import com.mussarrellos.backend.modules.authentication.application.dtos.RSAKeyDto;
import com.mussarrellos.backend.modules.authentication.application.dtos.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthModule authModule;

    @PostMapping(value = "/publicKey", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RSAKeyDto> generateRSAKey(@Valid @RequestBody GenerateRSAKeyRequest request) {
        return authModule.executeCommand(new GenerateRSAKeyCommand(request.timeout()));
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TokenDto> authenticate(@RequestBody AuthenticateClientRequest request) {
        return authModule.executeCommand(
                new AuthenticateClientCommand(request.email(), request.password(),
                        request.inst(), request.fingerprint(), request.deviceId())
        );
    }
}
