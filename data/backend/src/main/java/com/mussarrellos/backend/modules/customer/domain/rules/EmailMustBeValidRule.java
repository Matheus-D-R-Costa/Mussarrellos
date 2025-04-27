package com.mussarrellos.backend.modules.client.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import com.mussarrellos.backend.modules.client.domain.specification.EmailSpecification;
import lombok.RequiredArgsConstructor;

/**
 * Regra que verifica se um endereço de email é válido em termos de formato.
 * Utiliza o padrão Specification para validação do formato do email.
 */
@RequiredArgsConstructor
public class EmailMustBeValidRule implements IBusinessRule {
    
    private final String email;
    // Usando o padrão Specification para encapsular as regras de validação
    private final EmailSpecification emailSpecification;
    
    /**
     * Construtor padrão que utiliza a especificação base de email.
     * 
     * @param email O email a ser validado
     */
    public EmailMustBeValidRule(String email) {
        this(email, new EmailSpecification());
    }
    
    /**
     * Construtor que também verifica se o email não é descartável.
     * 
     * @param email O email a ser validado
     * @param requireNonDisposable Se true, emails de serviços descartáveis serão rejeitados
     */
    public EmailMustBeValidRule(String email, boolean requireNonDisposable) {
        this(email, requireNonDisposable ? 
                EmailSpecification.nonDisposable() : 
                new EmailSpecification());
    }

    @Override
    public boolean isBroken() {
        // Usa a especificação para fazer a validação
        return !emailSpecification.isSatisfiedBy(email);
    }

    @Override
    public String getMessage() {
        return "O endereço de email '" + email + "' não é válido.";
    }
}
