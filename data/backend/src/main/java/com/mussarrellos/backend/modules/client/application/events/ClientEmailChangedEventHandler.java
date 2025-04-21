package com.mussarrellos.backend.modules.client.application.events;

import com.mussarrellos.backend.buildingblocks.application.result.Result;
import com.mussarrellos.backend.modules.client.domain.events.ClientEmailChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para o evento de domínio ClientEmailChangedEvent.
 * Demonstra o uso do padrão Observer para reagir a eventos de domínio.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEmailChangedEventHandler {

    /**
     * Processa o evento de alteração de email do cliente.
     * Atualiza sistemas externos e envia notificações.
     * 
     * @param event O evento de alteração de email
     * @return Um Result indicando sucesso ou falha no processamento
     */
    @EventListener
    public Mono<Result<Void>> handle(ClientEmailChangedEvent event) {
        log.info("Email alterado para o cliente ID: {} de {} para {}", 
                event.getClientId(), event.getOldEmail(), event.getNewEmail());
        
        return Mono.defer(() -> {
            try {
                // 1. Enviar email de confirmação de alteração
                sendConfirmationEmail(event);
                
                // 2. Atualizar sistemas externos
                updateExternalSystems(event);
                
                // 3. Registrar alteração para auditoria
                logForAudit(event);
                
                return Mono.just(Result.success(null));
            } catch (Exception e) {
                log.error("Erro ao processar evento de alteração de email: {}", e.getMessage(), e);
                return Mono.just(Result.failure("Falha ao processar evento: " + e.getMessage()));
            }
        });
    }
    
    /**
     * Envia um email de confirmação da alteração.
     */
    private void sendConfirmationEmail(ClientEmailChangedEvent event) {
        // Implementação simulada
        log.debug("Enviando email de confirmação para o novo endereço: {}", event.getNewEmail());
        
        // Em um cenário real, usaria um serviço de email
        // emailService.send(new EmailMessage(event.getNewEmail(), "Seu email foi alterado", 
        //     "Seu endereço de email foi alterado de " + event.getOldEmail() + " para " + event.getNewEmail()));
    }
    
    /**
     * Atualiza sistemas externos com o novo email.
     */
    private void updateExternalSystems(ClientEmailChangedEvent event) {
        // Implementação simulada
        log.debug("Atualizando email em sistemas externos para o cliente: {}", event.getClientId());
        
        // Em um cenário real, faria chamadas a serviços externos
        // notificationService.updateUserEmail(event.getClientId().toString(), event.getNewEmail());
        // marketingService.updateContact(event.getOldEmail(), event.getNewEmail());
    }
    
    /**
     * Registra a alteração para fins de auditoria.
     */
    private void logForAudit(ClientEmailChangedEvent event) {
        log.debug("Registrando alteração de email para auditoria: {}", event.getId());
        
        // Em um cenário real, registraria em um sistema de auditoria
        // auditLogService.log(
        //     new AuditLogEntry(
        //         event.getClientId(), 
        //         "EMAIL_CHANGED",
        //         Map.of("oldEmail", event.getOldEmail(), "newEmail", event.getNewEmail()),
        //         event.getOccurredOn()
        //     )
        // );
    }
} 