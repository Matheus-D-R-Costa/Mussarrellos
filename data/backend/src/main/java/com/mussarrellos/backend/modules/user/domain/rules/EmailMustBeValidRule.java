package com.mussarrellos.backend.modules.user.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

/**
 * Regra que verifica se um endereço de email é válido em termos de formato.
 */
@RequiredArgsConstructor
public class EmailMustBeValidRule implements IBusinessRule {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private final String email;

    @Override
    public boolean isBroken() {
        return email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public String getMessage() {
        return "O endereço de email '" + email + "' não é válido.";
    }
}
