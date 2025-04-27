package com.mussarrellos.backend.buildingblocks.application.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxCleanupService {
    
    private final DatabaseClient databaseClient;

    @Scheduled(cron = "${outbox.cleanup-cron:0 0 0 * * ?}")
    public void cleanupOutboxMessages() {
        log.info("Iniciando limpeza automática da tabela outbox_messages");
        databaseClient.sql("SELECT cleanup_processed_outbox_messages()")
            .map((row, metadata) -> row.get(0, Integer.class))
            .one()
            .doOnSuccess(count -> {
                if (count != null && count > 0) {
                    log.info("Limpeza da outbox concluída: {} mensagens removidas", count);
                } else {
                    log.info("Limpeza da outbox concluída: nenhuma mensagem removida");
                }
            })
            .doOnError(error -> log.error("Erro ao executar limpeza da outbox", error))
            .onErrorResume(error -> Mono.empty())
            .subscribe();
    }

} 