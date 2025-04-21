package com.mussarrellos.backend.buildingblocks.application.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Serviço responsável pela limpeza automática da tabela de outbox.
 * Remove periodicamente mensagens processadas antigas para evitar crescimento indefinido da tabela.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxCleanupService {
    
    private final DatabaseClient databaseClient;
    
    /**
     * Executa limpeza diária (à meia-noite) de mensagens processadas antigas.
     * Utiliza a função do banco de dados para limpar de acordo com as configurações.
     */
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
    
    /**
     * Configura o período de retenção para as mensagens processadas.
     * 
     * @param retentionDays Número de dias para manter mensagens processadas
     * @return Mono que completa quando a configuração for salva
     */
    public Mono<Void> configureRetentionPeriod(int retentionDays) {
        if (retentionDays < 1) {
            return Mono.error(new IllegalArgumentException("O período de retenção deve ser maior que zero"));
        }
        
        log.info("Configurando período de retenção da outbox para {} dias", retentionDays);
        
        return databaseClient.sql(
            "UPDATE outbox_retention_config SET retention_days = :retentionDays WHERE id = " +
            "(SELECT id FROM outbox_retention_config LIMIT 1)")
            .bind("retentionDays", retentionDays)
            .fetch()
            .rowsUpdated()
            .then();
    }
    
    /**
     * Habilita ou desabilita a limpeza automática.
     * 
     * @param enabled Se a limpeza automática deve estar habilitada
     * @return Mono que completa quando a configuração for salva
     */
    public Mono<Void> setCleanupEnabled(boolean enabled) {
        log.info("{} limpeza automática da outbox", enabled ? "Habilitando" : "Desabilitando");
        
        return databaseClient.sql(
            "UPDATE outbox_retention_config SET enabled = :enabled WHERE id = " +
            "(SELECT id FROM outbox_retention_config LIMIT 1)")
            .bind("enabled", enabled)
            .fetch()
            .rowsUpdated()
            .then();
    }
} 