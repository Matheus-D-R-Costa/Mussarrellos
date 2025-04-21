package com.mussarrellos.backend.modules.authentication.application.commands.handler;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.buildingblocks.application.result.Error;
import com.mussarrellos.backend.buildingblocks.application.result.Result;
import com.mussarrellos.backend.modules.authentication.application.commands.AuthenticateClientCommand;
// Remove IAuthModule import if no longer needed for other purposes in this handler
// import com.mussarrellos.backend.modules.authentication.application.contracts.IAuthModule; 
import com.mussarrellos.backend.modules.authentication.application.dtos.TokenDto;
import com.mussarrellos.backend.modules.authentication.domain.AuthenticationException;
// Keep direct dependency interface
import com.mussarrellos.backend.modules.client.application.contracts.IClientModule; 
import com.mussarrellos.backend.modules.client.application.dtos.ClientCredentialsDto;
import com.mussarrellos.backend.modules.client.application.queries.FindClientCredentialsByEmailQuery; 
// TODO: Add import for ICryptoModule when needed
// import com.mussarrellos.backend.modules.cryptography.application.contracts.ICryptoModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para o comando de autenticação de cliente.
 * Implementa lógica de autenticação com padrão Result para controle de fluxo de erro.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticateClientCommandHandler implements ICommandHandler<AuthenticateClientCommand, TokenDto> {

    // Inject the specific module interfaces needed
    private final IClientModule clientModule; 
    // TODO: Inject ICryptoModule when token generation is implemented
    // private final ICryptoModule cryptoModule;

    @Override
    public Mono<TokenDto> handle(AuthenticateClientCommand command) {
        log.debug("Handling AuthenticateClientCommand for email: {}", command.email());

        return fetchClientCredentials(command.email())
                .flatMap(credentialsResult -> {
                    if (!credentialsResult.isSuccess()) {
                        return Mono.error(new AuthenticationException("Cliente não encontrado"));
                    }
                    
                    return verifyPassword(credentialsResult.getValue(), command.password())
                            .flatMap(passwordResult -> {
                                if (!passwordResult.isSuccess()) {
                                    return Mono.error(new AuthenticationException(
                                            passwordResult.getErrors().get(0).getMessage()));
                                }
                                
                                return generateToken(passwordResult.getValue(), command);
                            });
                });
    }

    // Removed findClientModule() as clientModule is now injected directly
    
    /**
     * Busca as credenciais do cliente pelo email.
     * Retorna um Result contendo as credenciais ou um erro.
     */
    private Mono<Result<ClientCredentialsDto>> fetchClientCredentials(String email) {
        var query = new FindClientCredentialsByEmailQuery(email);
        log.debug("Executing query: {}", query.getClass().getSimpleName());
        
        return clientModule.executeQuery(query)
                .map(credentials -> Result.success(credentials))
                .switchIfEmpty(Mono.just(Result.failure(
                        new Error("Cliente não encontrado", "CLIENT_NOT_FOUND"))));
    }

    /**
     * Verifica se a senha fornecida corresponde à senha armazenada.
     * Retorna um Result indicando sucesso ou falha na verificação.
     */
    private Mono<Result<ClientCredentialsDto>> verifyPassword(ClientCredentialsDto credentials, String providedPassword) {
        log.debug("Verifying password for client ID: {}", credentials.clientId());
        
        // TODO: Implementar verificação real de senha
        // Exemplo: boolean passwordsMatch = passwordEncoder.matches(providedPassword, credentials.hashedPassword());
        boolean passwordsMatch = true; // Placeholder
        
        if (passwordsMatch) {
            log.debug("Password verification successful for client ID: {}", credentials.clientId());
            return Mono.just(Result.success(credentials));
        } else {
            log.warn("Password verification failed for client ID: {}", credentials.clientId());
            return Mono.just(Result.failure(
                    new Error("Credenciais inválidas", "INVALID_CREDENTIALS")));
        }
    }
    
    /**
     * Gera um token de autenticação para o cliente.
     */
    private Mono<TokenDto> generateToken(ClientCredentialsDto credentials, AuthenticateClientCommand command) {
        log.debug("Generating token for client ID: {}", credentials.clientId());
        
        // Placeholder para geração real de token
        // TODO: Implementar lógica real de geração de token usando o módulo de criptografia
        return Mono.just(new TokenDto(
                "dummy-access-token-" + credentials.clientId(), 
                "dummy-refresh-token-" + credentials.clientId()
        ));
    }
}
