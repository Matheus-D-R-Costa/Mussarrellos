package com.mussarrellos.backend.modules.client.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import com.mussarrellos.backend.modules.client.domain.specification.PasswordSpecification;
import lombok.RequiredArgsConstructor;

/**
 * Verifica se uma senha atende aos requisitos mínimos de segurança.
 * Usa o padrão Specification para encapsular a lógica de validação.
 */
@RequiredArgsConstructor
public class PasswordMustBeStrongRule implements IBusinessRule {
    
    private final String password;
    // Usando o padrão Specification para validação
    private final PasswordSpecification passwordSpecification;
    
    /**
     * Construtor padrão usando a especificação básica de senha.
     * 
     * @param password A senha a ser validada
     */
    public PasswordMustBeStrongRule(String password) {
        this(password, new PasswordSpecification());
    }
    
    /**
     * Construtor que utiliza a especificação de complexidade.
     * 
     * @param password A senha a ser validada
     * @param requireComplexity Se true, verificações adicionais de complexidade serão aplicadas
     */
    public PasswordMustBeStrongRule(String password, boolean requireComplexity) {
        this(password, requireComplexity ? 
                PasswordSpecification.withComplexity() : 
                new PasswordSpecification());
    }

    @Override
    public boolean isBroken() {
        return !passwordSpecification.isSatisfiedBy(password);
    }

    @Override
    public String getMessage() {
        return "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um caractere especial e ter no mínimo 6 caracteres.";
    }
}
