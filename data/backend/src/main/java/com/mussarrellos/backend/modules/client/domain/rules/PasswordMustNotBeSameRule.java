package com.mussarrellos.backend.modules.client.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import com.mussarrellos.backend.modules.client.domain.specification.PasswordSpecification;
import lombok.RequiredArgsConstructor;

/**
 * Regra que verifica se a nova senha não é igual à senha atual.
 * Usa o padrão Specification para encapsular a lógica de validação.
 */
@RequiredArgsConstructor
public class PasswordMustNotBeSameRule implements IBusinessRule {
    private final String newPassword;
    private final PasswordSpecification passwordSpecification;
    
    /**
     * Construtor que cria a regra usando um verificador de correspondência de senha.
     * 
     * @param newPassword A nova senha
     * @param matchChecker O verificador de correspondência da senha atual
     */
    public PasswordMustNotBeSameRule(String newPassword, PasswordMatchChecker matchChecker) {
        this(newPassword, PasswordSpecification.notMatchingExisting(matchChecker::matches));
    }

    @Override
    public boolean isBroken() {
        return !passwordSpecification.isSatisfiedBy(newPassword);
    }

    @Override
    public String getMessage() {
        return "A nova senha não pode ser igual à senha atual.";
    }

    /**
     * Interface funcional para verificação de correspondência de senha.
     */
    @FunctionalInterface
    public interface PasswordMatchChecker {
        boolean matches(String password);
    }
} 