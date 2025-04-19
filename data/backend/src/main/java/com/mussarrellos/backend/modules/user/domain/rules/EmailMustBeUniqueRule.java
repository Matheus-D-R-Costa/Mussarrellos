package com.mussarrellos.backend.modules.user.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;

import com.mussarrellos.backend.modules.user.domain.entity.Client;
import lombok.RequiredArgsConstructor;

/**
 * Regra de negócio que verifica se um email é único.
 */
@RequiredArgsConstructor
public class EmailMustBeUniqueRule implements IBusinessRule {

    private final String email;
    private final Client.EmailUniquenessChecker checker;

    @Override
    public boolean isBroken() {
        return !checker.isEmailUnique(email);
    }

    @Override
    public String getMessage() {
        return "Email '" + email + "' is already in use";
    }
} 