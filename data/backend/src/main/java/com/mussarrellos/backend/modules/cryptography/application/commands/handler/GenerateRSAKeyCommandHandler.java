package com.mussarrellos.backend.modules.cryptography.application.commands.handler;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.modules.cryptography.application.commands.GenerateRSAKeyCommand;
import com.mussarrellos.backend.modules.cryptography.application.dtos.RSAKeyDto;
import com.mussarrellos.backend.modules.cryptography.domain.services.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * Handler para o comando de geração de chave RSA interno ao módulo Cryptography.
 * Este handler lida com comandos internos e depende diretamente do serviço de domínio.
 */
@Component
@RequiredArgsConstructor
public class GenerateRSAKeyCommandHandler implements ICommandHandler<GenerateRSAKeyCommand, RSAKeyDto> {

    private final CryptoService cryptoService;

    @Override
    public Mono<RSAKeyDto> handle(GenerateRSAKeyCommand command) {
        return cryptoService.generateRSAKeyPair(command.timeout())
                .map(keyPair -> {
                    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                    return new RSAKeyDto(
                            publicKey.getModulus(),
                            publicKey.getAlgorithm(),
                            publicKey.getPublicExponent()
                    );
                });
    }
}
