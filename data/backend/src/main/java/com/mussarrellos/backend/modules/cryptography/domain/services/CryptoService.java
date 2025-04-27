package com.mussarrellos.backend.modules.cryptography.domain.services;

import com.mussarrellos.backend.modules.cryptography.domain.factory.KeyPairGeneratorFactory;
import com.mussarrellos.backend.modules.cryptography.domain.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.KeyPair;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;

    public Mono<KeyPair> generateRSAKeyPair(int timeoutMinutes) {
        if (timeoutMinutes <= 0) {
            return Mono.error(new IllegalArgumentException("O timeout deve ser maior que zero"));
        }
        
        return Mono.fromCallable(this::generateKeyPair)
                .flatMap(keyPair -> {
                    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(timeoutMinutes);
                    log.debug("Gerando nova chave RSA com expiração em {}", expiresAt);
                    
                    return cryptoRepository.storeKeys(keyPair.getPublic(), keyPair.getPrivate(), expiresAt)
                            .thenReturn(keyPair)
                            .doOnSuccess(kp -> log.debug("Chave RSA armazenada com sucesso"))
                            .doOnError(e -> log.error("Erro ao armazenar chave RSA", e));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> keyExistsByPublicKey(PublicKey publicKey) {
        if (publicKey == null) {
            return Mono.error(new IllegalArgumentException("A chave pública não pode ser nula"));
        }
        
        return Mono.fromCallable(() -> cryptoRepository.existsValidKey(publicKey).block())
                .subscribeOn(Schedulers.boundedElastic());
    }

    private KeyPair generateKeyPair() {
        try {
            return KeyPairGeneratorFactory.createKeyPairGenerator().generateKeyPair();
        } catch (RuntimeException e) {
            log.error("Falha ao gerar par de chaves RSA", e);
        }
        return null;
    }
}
