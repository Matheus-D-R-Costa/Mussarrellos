package com.mussarrellos.backend.modules.cryptography.domain.repository;


import reactor.core.publisher.Mono;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;

public interface CryptoRepository {

    Mono<Void> storeKeys(PublicKey publicKey, PrivateKey privateKey, LocalDateTime expiresAt);
    Mono<Boolean> existsValidKey(PublicKey publicKey);

}
