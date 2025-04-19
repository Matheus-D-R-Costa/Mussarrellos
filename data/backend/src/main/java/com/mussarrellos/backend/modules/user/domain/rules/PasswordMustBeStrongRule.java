package com.mussarrellos.backend.modules.user.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * Verifica se uma senha atende aos requisitos mínimos de segurança.
 * Exige pelo menos: uma letra maiúscula, uma minúscula, um dígito e um caractere especial.
 */
public class PasswordMustBeStrongRule implements IBusinessRule {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{6,}$"
    );
    
    private final String password;

    public PasswordMustBeStrongRule(String password) {
        this.password = password;
    }

    @Override
    public boolean isBroken() {
        return password == null || password.isBlank() || !PASSWORD_PATTERN.matcher(password).matches();
    }

    @Override
    public String getMessage() {
        return "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um caractere especial e ter no mínimo 6 caracteres.";
    }
}
