package com.mussarrellos.backend.modules.customer.domain.events.handler;

import com.mussarrellos.backend.modules.customer.domain.events.CustomerCreatedDomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Exemplo de listener para um evento de domínio.
 * Este é o equivalente a um handler no contexto do MediatR em C#.
 */
@Slf4j
@Component
public class ClientCreatedDomainEventListener {

    /**
     * Método que trata o evento ClientCreatedDomainEvent.
     * @param event O evento capturado
     */
    @EventListener
    public void handle(CustomerCreatedDomainEvent event) {
        log.info("User created event received: User {} with ID {} was created at {}",
                event.getEmail(),
                event.getUserId(),
                event.getOccurredOn());
        
        // Aqui você implementaria a lógica de tratamento do evento
        // Como enviar e-mail de boas-vindas, notificar outros sistemas, etc.
    }
} 