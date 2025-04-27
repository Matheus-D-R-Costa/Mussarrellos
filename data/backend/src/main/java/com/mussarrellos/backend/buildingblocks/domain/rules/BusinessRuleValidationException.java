package com.mussarrellos.backend.buildingblocks.domain.rules;

import lombok.Getter;

@Getter
public class BusinessRuleValidationException extends RuntimeException {

    private final IBusinessRule brokenRule;
    private final String details;

    public BusinessRuleValidationException(IBusinessRule brokenRule) {
        super(brokenRule.getMessage());
        this.brokenRule = brokenRule;
        this.details = brokenRule.getMessage();
    }

    @Override
    public String toString() {
        return brokenRule.getClass().getName() + ": " + brokenRule.getMessage();
    }
}
