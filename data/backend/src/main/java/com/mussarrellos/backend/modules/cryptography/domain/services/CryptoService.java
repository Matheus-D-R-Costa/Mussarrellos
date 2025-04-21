package com.mussarrellos.backend.modules.cryptography.domain.services;

import com.mussarrellos.backend.modules.cryptography.domain.exceptions.CryptoOperationException;
import com.mussarrellos.backend.modules.cryptography.domain.factory.KeyPairGeneratorFactory;
import com.mussarrellos.backend.modules.cryptography.domain.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.KeyPair;
import java.security.PublicKey;
import java.time.LocalDateTime;

/**
 * Serviço de domínio para operações criptográficas.
 * Encapsula a lógica de negócio relacionada à criptografia.
 */
@Slf4j
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;

    /**
     * Gera um novo par de chaves RSA e armazena por um tempo específico.
     *
     * @param timeoutMinutes Tempo em minutos que a chave ficará disponível
     * @return Um Mono que emite o par de chaves gerado
     * @throws CryptoOperationException Se ocorrer algum erro na geração ou armazenamento das chaves
     */
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
                .onErrorMap(e -> 
                    !(e instanceof CryptoOperationException) ? 
                    new CryptoOperationException("Falha na operação criptográfica: " + e.getMessage(), e) : e
                )
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Verifica se existe uma chave válida para uma dada chave pública.
     *
     * @param publicKey A chave pública para verificar
     * @return Um Mono que emite true se a chave existir e for válida, false caso contrário
     */
    public Mono<Boolean> keyExistsByPublicKey(PublicKey publicKey) {
        if (publicKey == null) {
            return Mono.error(new IllegalArgumentException("A chave pública não pode ser nula"));
        }
        
        return Mono.fromCallable(() -> cryptoRepository.existsValidKey(publicKey).block())
                .onErrorMap(e -> new CryptoOperationException("Erro ao verificar existência da chave: " + e.getMessage(), e))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Gera um novo par de chaves RSA.
     *
     * @return O par de chaves gerado
     * @throws CryptoOperationException Se ocorrer algum erro na geração das chaves
     */
    private KeyPair generateKeyPair() {
        try {
            return KeyPairGeneratorFactory.createKeyPairGenerator().generateKeyPair();
        } catch (RuntimeException e) {
            log.error("Falha ao gerar par de chaves RSA", e);
            throw new CryptoOperationException("Falha ao gerar par de chaves RSA: " + e.getMessage(), e);
        }
    }
}
