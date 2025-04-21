package com.mussarrellos.backend.modules.authentication.application.commands.handler;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.modules.authentication.application.commands.GenerateRSAKeyCommand;
import com.mussarrellos.backend.modules.authentication.application.dtos.RSAKeyDto;
import com.mussarrellos.backend.modules.cryptography.application.contracts.ICryptoModule;
import com.mussarrellos.backend.modules.cryptography.application.integration.contracts.GenerateRSAKeyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para o comando de geração de chave RSA.
 * Demonstra o padrão de comunicação entre módulos usando contratos explícitos de integração (ACL).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateRSAKeyCommandHandler implements ICommandHandler<GenerateRSAKeyCommand, RSAKeyDto> {

    private final ICryptoModule cryptoModule;

    @Override
    public Mono<RSAKeyDto> handle(GenerateRSAKeyCommand command) {
        log.debug("Gerando par de chaves RSA com timeout de {} minutos", command.timeout());
        
        // 1. Cria um request usando o contrato de integração do módulo Cryptography
        var request = new GenerateRSAKeyRequest(command.timeout());
        
        // 2. Chama o serviço de integração explícito, não o comando interno do outro módulo
        return cryptoModule.getCryptoIntegrationService().generateRSAKey(request)
                // 3. Mapeia a resposta da integração para o DTO específico deste módulo
                .map(response -> new RSAKeyDto(
                        response.modulus(),
                        response.algorithm(),
                        response.publicExponent()
                ))
                .doOnSuccess(keyDto -> log.debug("Chave RSA gerada com sucesso. Algoritmo: {}", keyDto.algorithm()))
                .doOnError(error -> log.error("Erro ao gerar chave RSA: {}", error.getMessage()));
    }
}
