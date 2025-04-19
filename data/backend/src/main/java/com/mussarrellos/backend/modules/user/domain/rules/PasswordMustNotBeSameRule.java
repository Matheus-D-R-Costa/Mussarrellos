package com.mussarrellos.backend.modules.user.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;

/**
 * Regra que verifica se a nova senha não é igual à senha atual.
 */
@RequiredArgsConstructor
public class PasswordMustNotBeSameRule implements IBusinessRule {
    private final String newPassword;
    private final PasswordMatchChecker matchChecker;

    @Override
    public boolean isBroken() {
        return matchChecker.matches(newPassword);
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