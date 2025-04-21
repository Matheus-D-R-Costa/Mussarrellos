package com.mussarrellos.backend.modules.authentication.api;

import com.mussarrellos.backend.modules.authentication.api.requests.AuthenticationRequest;
import com.mussarrellos.backend.modules.authentication.api.requests.GenerateRSAKeyRequest;
import com.mussarrellos.backend.modules.authentication.application.commands.AuthenticateClientCommand;
import com.mussarrellos.backend.modules.authentication.application.commands.GenerateRSAKeyCommand;
import com.mussarrellos.backend.modules.authentication.application.contracts.IAuthModule;
import com.mussarrellos.backend.modules.authentication.application.dtos.RSAKeyDto;
import com.mussarrellos.backend.modules.authentication.application.dtos.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "APIs para autenticação e gerenciamento de chaves")
public class AuthController {

    private final IAuthModule authModule;
    
    @Operation(
        summary = "Gerar par de chaves RSA",
        description = "Gera um novo par de chaves RSA. A chave pública é retornada ao cliente, " +
                      "enquanto a chave privada é armazenada temporariamente no servidor pelo tempo especificado.",
        responses = {
            @ApiResponse(
                responseCode = "201", 
                description = "Chave RSA gerada com sucesso",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RSAKeyDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Parâmetros inválidos",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                responseCode = "500", 
                description = "Erro interno do servidor",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
        }
    )
    @PostMapping(value = "/publicKey", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RSAKeyDto> generateRSAKey(
            @Parameter(description = "Configuração para geração de chave, incluindo o tempo de validade em minutos", required = true)
            @Valid @RequestBody GenerateRSAKeyRequest request) {
        return authModule.executeCommand(new GenerateRSAKeyCommand(request.timeout()));
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TokenDto> authenticate(@RequestBody AuthenticationRequest request) {
        return authModule.executeCommand(
                new AuthenticateClientCommand(request.email(), request.password(),
                        request.inst(), request.fingerprint(), request.deviceId())
        );
    }
}
