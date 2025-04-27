package com.mussarrellos.backend.modules.customer.domain.rules;

import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;
import com.mussarrellos.backend.modules.customer.domain.specifications.PasswordSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordMustNotBeSameRule implements IBusinessRule {

    private final String newPassword;
    private final PasswordSpecification passwordSpecification;

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

    @FunctionalInterface
    public interface PasswordMatchChecker {
        boolean matches(String password);
    }
} 