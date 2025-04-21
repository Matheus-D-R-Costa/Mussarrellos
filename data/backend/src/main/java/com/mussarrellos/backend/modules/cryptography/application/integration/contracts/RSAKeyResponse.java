package com.mussarrellos.backend.modules.cryptography.application.integration.contracts;

import java.math.BigInteger;

/**
 * Contrato de integração para resposta de geração de chave RSA.
 * Este DTO é explicitamente exposto para uso por outros módulos.
 */
public record RSAKeyResponse(
        BigInteger modulus,
        String algorithm,
        BigInteger publicExponent
) {} 