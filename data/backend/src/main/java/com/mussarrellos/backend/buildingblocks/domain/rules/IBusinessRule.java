package com.mussarrellos.backend.buildingblocks.domain.rules;

public interface IBusinessRule {

    boolean isBroken();

    String getMessage();

}
