package com.mussarrellos.backend.modules.customer.domain.rules;

import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;
import com.mussarrellos.backend.modules.customer.domain.specifications.EmailSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailMustBeValidRule implements IBusinessRule {
    
    private final String email;
    private final EmailSpecification emailSpecification;
    

    public EmailMustBeValidRule(String email) {
        this(email, new EmailSpecification());
    }
    

    public EmailMustBeValidRule(String email, boolean requireNonDisposable) {
        this(email, requireNonDisposable ? 
                EmailSpecification.nonDisposable() : 
                new EmailSpecification());
    }

    @Override
    public boolean isBroken() {
        return !emailSpecification.isSatisfiedBy(email);
    }

    @Override
    public String getMessage() {
        return "O endereço de email '" + email + "' não é válido.";
    }
}
