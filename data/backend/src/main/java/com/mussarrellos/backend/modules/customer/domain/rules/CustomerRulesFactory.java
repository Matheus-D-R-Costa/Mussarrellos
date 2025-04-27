package com.mussarrellos.backend.modules.customer.domain.rules;

import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;
import com.mussarrellos.backend.modules.customer.domain.entities.Customer.EmailUniquenessChecker;
import com.mussarrellos.backend.modules.customer.domain.rules.PasswordMustNotBeSameRule.PasswordMatchChecker;
import com.mussarrellos.backend.modules.customer.domain.specifications.PasswordSpecification;

public final class CustomerRulesFactory {
    
    // Construtor privado para evitar instanciação
    private CustomerRulesFactory() {}

    public static IBusinessRule emailMustBeValid(String email) {
        return new EmailMustBeValidRule(email);
    }

    public static IBusinessRule emailMustBeUnique(String email, EmailUniquenessChecker checker) {
        return new EmailMustBeUniqueRule(email, checker);
    }

    public static IBusinessRule passwordMustBeStrong(String password) {
        return new PasswordMustBeStrongRule(password);
    }

    public static IBusinessRule passwordMustBeStrong(String password, boolean requireComplexity) {
        return new PasswordMustBeStrongRule(password, requireComplexity);
    }

    public static IBusinessRule passwordMustBeStrong(String password, PasswordSpecification passwordSpecification) {
        return new PasswordMustBeStrongRule(password, passwordSpecification);
    }

    public static IBusinessRule passwordMustNotBeSame(String newPassword, PasswordMatchChecker passwordMatchChecker) {
        return new PasswordMustNotBeSameRule(newPassword, passwordMatchChecker);
    }
} 