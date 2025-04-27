package com.mussarrellos.backend.modules.customer.domain.rules;

import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;

import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailMustBeUniqueRule implements IBusinessRule {

    private final String email;
    private final Customer.EmailUniquenessChecker checker;

    @Override
    public boolean isBroken() {
        return !checker.isEmailUnique(email);
    }

    @Override
    public String getMessage() {
        return "Email '" + email + "' is already in use";
    }
} 