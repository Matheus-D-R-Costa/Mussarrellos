package com.mussarrellos.backend.modules.cryptography.application.integration;

import com.mussarrellos.backend.modules.cryptography.application.integration.contracts.GenerateRSAKeyRequest;
import com.mussarrellos.backend.modules.cryptography.application.integration.contracts.RSAKeyResponse;
import reactor.core.publisher.Mono;

/**
 * Interface de integração que define operações criptográficas expostas para outros módulos.
 * Este contrato define explicitamente as operações que outros módulos podem utilizar,
 * funcionando como uma Anti-Corruption Layer (ACL).
 */
public interface CryptoIntegrationService {
    /**
     * Gera um par de chaves RSA e retorna a parte pública.
     * 
     * @param request Parâmetros para geração da chave
     * @return Resposta contendo a chave pública gerada
     */
    Mono<RSAKeyResponse> generateRSAKey(GenerateRSAKeyRequest request);
} 