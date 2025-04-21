package com.mussarrellos.backend.modules.cryptography.infra;


import com.mussarrellos.backend.modules.cryptography.domain.repository.CryptoRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class CryptoRepositoryAdapterImpl implements CryptoRepository {

    private static final Map<PublicKey, KeyEntry> keyStore = new ConcurrentHashMap<>();
    private static final Scheduler BLOCKING_SCHEDULER = Schedulers.boundedElastic();
    private static final int ONE_HOUR = 1;
    private static final int MIN_REMOVED_KEYS_TO_LOG = 0;

    @PostConstruct
    private void initKeyCleanup() {
        Flux.interval(Duration.ZERO, Duration.ofHours(ONE_HOUR), Schedulers.parallel())
                .flatMap(_ -> removeExpiredKeys())
                .subscribe();
    }

    @Override
    public Mono<Void> storeKeys(PublicKey publicKey,
                                PrivateKey privateKey,
                                LocalDateTime expiresAt) {
        return Mono.fromRunnable(() -> {
                    log.debug("Storing key {} with expiration {}", publicKey, expiresAt);
                    keyStore.put(publicKey, new KeyEntry(privateKey, expiresAt));
                })
                .subscribeOn(BLOCKING_SCHEDULER)
                .then();
    }

    @Override
    public Mono<Boolean> existsValidKey(PublicKey publicKey) {
        return Mono.fromSupplier(() -> {
                    log.debug("Checking if key {} exists and is valid", publicKey);
                    return Optional.ofNullable(keyStore.get(publicKey))
                            .filter(KeyEntry::isValid)
                            .isPresent();
                })
                .subscribeOn(BLOCKING_SCHEDULER);
    }

    public Mono<Void> removeExpiredKeys() {
        return Mono.fromRunnable(() -> {
                    log.debug("Removing expired keys");
                    var now = LocalDateTime.now();
                    int before = keyStore.size();
                    keyStore.entrySet()
                            .removeIf(entry -> entry.getValue().isExpired(now));
                    int removed = before - keyStore.size();
                    if (removed > MIN_REMOVED_KEYS_TO_LOG) log.info("Removed {} expired RSA keys", removed);
                })
                .subscribeOn(BLOCKING_SCHEDULER)
                .then();
    }

    private record KeyEntry(@Getter
                            PrivateKey privateKey,
                            LocalDateTime expiresAt) {

        public boolean isValid() {
            return !expiresAt.isBefore(LocalDateTime.now());
        }

        public boolean isExpired(LocalDateTime referenceTime) {
            return expiresAt.isBefore(referenceTime);
        }
    }
}
