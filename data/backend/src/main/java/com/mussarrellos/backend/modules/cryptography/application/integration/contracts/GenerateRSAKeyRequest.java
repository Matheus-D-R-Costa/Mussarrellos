package com.mussarrellos.backend.modules.cryptography.application.integration.contracts;

/**
 * Contrato de integração para solicitação de geração de chave RSA.
 * Este contrato é explicitamente exposto para uso por outros módulos.
 */
public record GenerateRSAKeyRequest(int timeout) {
} 