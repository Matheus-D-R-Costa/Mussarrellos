package com.mussarrellos.backend.modules.cryptography.application.integration.impl;

import com.mussarrellos.backend.modules.cryptography.application.dtos.RSAKeyDto;
import com.mussarrellos.backend.modules.cryptography.application.integration.CryptoIntegrationService;
import com.mussarrellos.backend.modules.cryptography.application.integration.contracts.GenerateRSAKeyRequest;
import com.mussarrellos.backend.modules.cryptography.application.integration.contracts.RSAKeyResponse;
import com.mussarrellos.backend.modules.cryptography.domain.services.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * Implementação do serviço de integração para operações criptográficas.
 * Esta classe acessa diretamente os serviços de domínio sem passar pelo módulo.
 */
@Service
@RequiredArgsConstructor
public class CryptoIntegrationServiceImpl implements CryptoIntegrationService {

    // Injetar diretamente o serviço de domínio em vez do módulo
    private final CryptoService cryptoService;

    @Override
    public Mono<RSAKeyResponse> generateRSAKey(GenerateRSAKeyRequest request) {
        // Chamar diretamente o serviço de domínio
        return cryptoService.generateRSAKeyPair(request.timeout())
                .map(keyPair -> {
                    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                    // Criar DTO de integração diretamente
                    return new RSAKeyResponse(
                            publicKey.getModulus(),
                            publicKey.getAlgorithm(),
                            publicKey.getPublicExponent()
                    );
                });
    }
} 