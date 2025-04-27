package com.mussarrellos.backend.modules.customer.domain.rules;

import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;
import com.mussarrellos.backend.modules.customer.domain.specifications.PasswordSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordMustBeStrongRule implements IBusinessRule {
    
    private final String password;
    private final PasswordSpecification passwordSpecification;

    public PasswordMustBeStrongRule(String password) {
        this(password, new PasswordSpecification());
    }

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
